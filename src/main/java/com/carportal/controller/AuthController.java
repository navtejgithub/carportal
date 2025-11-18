package com.carportal.controller;

import com.carportal.entity.User;
import com.carportal.payload.LoginDto;
import com.carportal.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    private AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<?> createUser(@RequestBody User user){
        String user1 = authService.createUser(user);
        return new ResponseEntity<>(user1,HttpStatus.CREATED);
    }
    @GetMapping("/view")
    ResponseEntity<?> view(){
        return new ResponseEntity<>("Welcome to my WORLD",HttpStatus.ACCEPTED);
    }
    @GetMapping("/all")
    ResponseEntity<?> getUsersData(){
        return new ResponseEntity<>(authService.getUsers(),HttpStatus.CREATED);
    }
    @PostMapping("/login")
    ResponseEntity<?> verifyLogin(@RequestBody LoginDto dto){

        String authenticate = authService.authenticate(dto);
        if (authenticate!=null)
            return new ResponseEntity<>(authenticate, HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>("Invalid Username", HttpStatus.OK);
    }
//@PostMapping("/login")
//public ResponseEntity<?> verifyLogin(@RequestBody LoginDto dto){
//
//    String token = authService.authenticate(dto);
//
//    if(token == null){
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of("message", "Invalid username or password"));
//    }
//
//    return ResponseEntity.ok(Map.of(
//            "message", "Login success",
//            "token", token
//    ));
//}
}
