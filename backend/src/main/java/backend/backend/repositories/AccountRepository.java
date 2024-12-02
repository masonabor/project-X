package backend.backend.repositories;

import backend.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(long accountNumber);
    Optional<Account> findById(long id);
    boolean existsByAccountNumber(long accountNumber);
}
