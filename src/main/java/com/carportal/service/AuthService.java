package com.carportal.service;

import com.carportal.entity.User;
import com.carportal.payload.LoginDto;
import com.carportal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
//    public AuthService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    private UserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    public String createUser(User user) {


        Optional<User> opEmail=userRepository.findByEmail(user.getEmail());
        Optional<User> opMobile=userRepository.findByMobile(user.getMobile());
        Optional<User> opUsername=userRepository.findByUsername(user.getUsername());
        Optional<User> optionalUser= userRepository.findByEmailOrMobileOrUsername(user.getEmail(),user.getMobile(),user.getUsername());

        if (opEmail.isPresent()){
            return "Email already exists ";
        }
        if (opUsername.isPresent()){
            return "Username already exists ";
        }
        if (opMobile.isPresent()){
            return "Mobile already exists ";
        }

        if (optionalUser.isPresent()){
            return "User Details already exists ";
        }

//        String encode = passwordEncoder.encode(user.getPassword());
        String encode= BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5));//advisible compared to above
        user.setPassword(encode);
        userRepository.save(user);
        return "User Created Successfully";


    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String authenticate(LoginDto dto) {
        Optional<User> opUser=userRepository.findByUsername(dto.getUsername());
        if (opUser.isPresent()){
            boolean equals = BCrypt.checkpw(dto.getPassword(),opUser.get().getPassword());
            if(equals){
                return jwtService.generateToken(opUser.get().getUsername());
            }
//            return "true";
        }
        return "null";

    }
//public String authenticate(LoginDto dto) {
//
//    Optional<User> opUser = userRepository.findByUsername(dto.getUsername());
//
//    if (!opUser.isPresent()) {
//        return null; // username not found
//    }
//
//    User user = opUser.get();
//
//    boolean match = BCrypt.checkpw(dto.getPassword(), user.getPassword());
//
//    if (!match) {
//        return null; // wrong password
//    }
//
//    return jwtService.generateToken(user.getUsername());  // success -> return token
//}
}
