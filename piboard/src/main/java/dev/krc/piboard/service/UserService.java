package dev.krc.piboard.service;

import dev.krc.piboard.dto.UserLoginDto;
import dev.krc.piboard.dto.UserRegistrationDto;
import dev.krc.piboard.model.Users;
import dev.krc.piboard.repository.UsersRepository;
import dev.krc.piboard.util.JWTutil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bcrypt;
    private final JWTutil jwtutil;

    public UserService(UsersRepository usersRepository, BCryptPasswordEncoder bcrypt, JWTutil jwtutil) {
        this.usersRepository = usersRepository;
        this.bcrypt = bcrypt;
        this.jwtutil = jwtutil;
    }
    public ResponseEntity<String> login(UserLoginDto loginDto) {
        var optional = usersRepository.findByEmail(loginDto.getEmail());
        if(optional.isPresent() && bcrypt.matches(loginDto.getPassword(), optional.get().getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Wrong email or password.",HttpStatus.UNAUTHORIZED);
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

    public String generateJwtToken(String email) {
        return jwtutil.generateToken(email);
    }

    @Tool(name = "login to pi_board",
            description = "pi_board login function. Calling this function logs in the user, which allows them to use issue related functions. " +
                        "Returns a JWT token if login is successful" +
                        "Login is required to be ")
    public String login(String email, String password) {
        UserLoginDto loginDto = new UserLoginDto(email,password);
        ResponseEntity<String> stat = login(loginDto);
        if(stat.getStatusCode().is2xxSuccessful()) {
            return jwtutil.generateToken(email);
        }
        return stat.toString();
    }

    @Tool(name = "register to pi_board",
            description = "pi_board register function. Calling this function registers a new user if password and confirmPassword are the same.")
    public String register(String email, String password, String confirmPassword) {
        ResponseEntity<String> stat = register(new UserRegistrationDto(email,password,confirmPassword));
        if(stat.getStatusCode().is2xxSuccessful()) {
            return "Register successful. Please login";
        }
        return stat.toString();
    }
}
