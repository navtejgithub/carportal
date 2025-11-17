package com.carportal.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

//    @Value("${jwt.algorithm.key}")
//    private String algorithmKey;
//    @Value("${jwt.issuer}")
//    private String issuer;
//    @Value("${jwt.expiry.duration}")
//    private long expiry;

    private static final String SECRET_KEY = "hellobro";
    private static final long EXPIRATION_TIME = 86400000; // 1 day
    private static final String issuer="com.carportal";
    private Algorithm algorithm;
    @PostConstruct
    public void postConstruct() throws Exception{
        algorithm=Algorithm.HMAC256(SECRET_KEY);

    }
    public String generateToken(String username){
        return JWT.create()
                .withClaim("name",username)
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){
        DecodedJWT decodedJWT=
                JWT.require(algorithm)
                        .withIssuer(issuer)
                        .build()
                        .verify(token);
        return decodedJWT.getClaim("name").asString();
    }
}
