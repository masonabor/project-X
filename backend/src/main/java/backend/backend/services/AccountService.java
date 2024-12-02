package backend.backend.services;

import backend.backend.models.Account;
import backend.backend.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findByAccountNumber(long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    protected Account createAccount() {
        Account account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setBalance(0);
        return accountRepository.save(account);
    }

    private long generateUniqueAccountNumber() {
        Random random = new Random();
        long accountNumber;

        do {
            accountNumber = 1000000000L + random.nextLong(9000000000L);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    public void deposit(Account account, float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сума поповнення має бути додатною");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        log.info("Рахунок №{} поповнено на {} грн. Новий баланс: {}", account.getAccountNumber(), amount, account.getBalance());
    }

}
