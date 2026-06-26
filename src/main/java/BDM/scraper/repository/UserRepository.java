package BDM.scraper.repository;

import BDM.scraper.entity.User;
import BDM.scraper.entity.type.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByProviderIdAndProviderType(
            String providerId,
            AuthProviderType providerType
    );
}