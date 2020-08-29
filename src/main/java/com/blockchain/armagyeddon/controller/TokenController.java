package com.blockchain.armagyeddon.controller;

import java.io.IOException;

import com.blockchain.armagyeddon.service.TokenService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/total")
    public String token() throws Exception {
        return tokenService.totalSupply();
    }

    @GetMapping("/user-balance/{email}")
    public String getBalance(@PathVariable String email) throws Exception {

        return tokenService.getBalance(email);
    }

    @GetMapping("/gye-balance/{id}")
    public String getBalance(@PathVariable Long id) throws Exception {

        return tokenService.getBalance(id);
    }

    @GetMapping("/charge/{email}/{amount}")
    public String chargeToken(@PathVariable String email, 
        @PathVariable String amount) throws IOException {

        boolean result = tokenService.chargeToken(email, amount);

        if(!result)
            return "didn't work";

        return "done";
    }

    @GetMapping("/send/{from}/{to}/{amount}")
    public String sendToken(@PathVariable String from, 
        @PathVariable String to, @PathVariable String amount){

        boolean result = tokenService.sendToken(from, to, amount);

        if(!result)
            return "didn't work";

        return "done";
    }

    @GetMapping("/use/{email}/{amount}")
    public String useToken(@PathVariable String email,
        @PathVariable String amount){

        boolean result = tokenService.burnToken(email, amount);

        if(!result)
            return "didn't work";

        return "done";
    }
    
}