package com.carlos.produtosapi.produtos_api.repository;

import com.carlos.produtosapi.produtos_api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findByLogin(String login);

    Optional<Users> findById(Long id);


}
