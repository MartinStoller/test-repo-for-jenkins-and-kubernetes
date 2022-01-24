package de.example.haegertime.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.lastname like %:keyword% OR u.firstname like %:keyword% OR u.email like %:keyword%")
    List<User> findBylastByFirstbyEmail(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.email = :email")
        //TODO Cedrik: lieber Optional<User> returnen, sonst muss im Service überall Nullpointer checks gemacht werden.
     User getUserByEmail(@Param("email") String email);
    boolean existsByEmail(String email);

}
