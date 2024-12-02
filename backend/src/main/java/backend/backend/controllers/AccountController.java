package backend.backend.controllers;

import backend.backend.dtos.DepositDTO;
import backend.backend.jwt.JwtUtil;
import backend.backend.models.Account;
import backend.backend.models.User;
import backend.backend.services.AccountService;
import backend.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PatchMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestHeader("Authorization") String header,
            @RequestBody() DepositDTO depositDTO) {

        try {
            String token = jwtUtil.extractTokenFromHeader(header);
            String username = jwtUtil.extractUsername(token);

            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Користувач не знайдений"));

            Account account = user.getAccount();
            accountService.deposit(account, depositDTO.getAmount());

            log.info("Рахунок користувача '{}' поповнено на {} грн", username, depositDTO.getAmount());
            return ResponseEntity.ok("Рахунок успішно поповнено на " + depositDTO.getAmount() + " грн");
        } catch (IllegalArgumentException e) {
            log.warn("Невірна сума поповнення: {}", depositDTO.getAmount());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResponseStatusException e) {
            log.error("Помилка при поповненні: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            log.error("Невідома помилка: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Сталася помилка під час поповнення рахунку");
        }
    }
}

