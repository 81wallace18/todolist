package com.wallace.todolist.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserModel>> findAll() {
        List<UserModel> list = userRepository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel user) {
        var userModel = userRepository.findByUsername(user.getUsername());
        
        if(userModel != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja existe");
        }

        var passwordHashred = BCrypt.withDefaults()
        .hashToString(12, user.getPassword().toCharArray());
        
        user.setPassword(passwordHashred);

        var userCreated = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }


}
