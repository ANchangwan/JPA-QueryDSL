package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
