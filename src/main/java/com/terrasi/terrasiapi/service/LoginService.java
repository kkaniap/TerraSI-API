package com.terrasi.terrasiapi.service;

import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser(String username, String password){
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        return user.orElse(null);
    }
}
