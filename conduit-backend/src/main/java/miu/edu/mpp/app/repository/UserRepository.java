package miu.edu.mpp.app.repository;


import miu.edu.mpp.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    //        Optional<User> findByEmail(String email);
    List<User> findByEmailIn(List<String> emails);

    Optional<Object> findByEmail(String email);

    void deleteByEmail(String email);
}
