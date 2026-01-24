package com.example.url_shortner.repositories;

import com.example.url_shortner.models.User;
import lombok.Builder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
Optional<User> findByEmail(String email);
}
