package dev.krc.piboard.service;

import dev.krc.piboard.dto.UserLoginDto;
import dev.krc.piboard.dto.UserRegistrationDto;
import dev.krc.piboard.model.Users;
import dev.krc.piboard.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bcrypt;

    public UserService(UsersRepository usersRepository, BCryptPasswordEncoder bcrypt) {
        this.usersRepository = usersRepository;
        this.bcrypt = bcrypt;
    }
    public ResponseEntity<String> login(UserLoginDto loginDto) {
        var optional = usersRepository.findByEmail(loginDto.getEmail());
        if(optional.isPresent() && bcrypt.matches(loginDto.getPassword(), optional.get().getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> register(UserRegistrationDto registerDto) {
        if(registerDto.getEmail().isEmpty() || registerDto.getPassword().isEmpty() || registerDto.getConfirmPassword().isEmpty()) {
            return new ResponseEntity<>("Fields can not be empty. \n {email,password,confirmPassword}",HttpStatus.BAD_REQUEST);
        }

        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        if(usersRepository.findByEmail(registerDto.getEmail()).isPresent()) {
           return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        Users user = new Users();
        user.setEmail(registerDto.getEmail());
        user.setPassword(bcrypt.encode(registerDto.getPassword()));
        usersRepository.save(user);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }
}
