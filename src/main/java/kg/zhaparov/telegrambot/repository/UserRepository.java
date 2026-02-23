package kg.zhaparov.telegrambot.repository;

import kg.zhaparov.telegrambot.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
