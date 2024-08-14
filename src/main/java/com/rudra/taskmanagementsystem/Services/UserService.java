package com.rudra.taskmanagementsystem.Services;

import com.rudra.taskmanagementsystem.Exceptions.UserNotFoundException;
import com.rudra.taskmanagementsystem.Models.User;
import com.rudra.taskmanagementsystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with a username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return The newly registered user.
     */
    public User registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return An Optional containing the found user, or empty if no user was found.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId The ID of the user to search for.
     * @return An Optional containing the found user, or empty if no user was found.
     */
    public Optional<User> findById(Long userId) throws UserNotFoundException{
        return Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found")));
    }
}
