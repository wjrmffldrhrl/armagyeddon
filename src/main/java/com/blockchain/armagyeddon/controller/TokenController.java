package com.blockchain.armagyeddon.controller;

import java.io.IOException;
import java.security.Principal;

import com.blockchain.armagyeddon.domain.dto.SendTokenDto;
import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import com.blockchain.armagyeddon.domain.repository.UserInfoRepository;
import com.blockchain.armagyeddon.service.GyeService;
import com.blockchain.armagyeddon.service.TokenService;

import com.blockchain.armagyeddon.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final GyeService gyeService;
    private final UserInfoService userinfoService;

    @GetMapping("/total")
    public String token() throws Exception {
        return tokenService.totalSupply();
    }

    @GetMapping("/user-token/{email}")
    public String getBalance(@PathVariable String email) throws Exception {

        return tokenService.getBalance(email);
    }

    @GetMapping("/gye-token/{id}")
    public String getBalance(@PathVariable Long id) throws Exception {

        return tokenService.getBalance(id);
    }

    // 토큰 충전
    @PostMapping("/user-token")
    public String chargeToken(@RequestBody String amount, Principal userInfo) throws IOException {
        System.out.println(amount);
        boolean result = tokenService.chargeToken(userInfo.getName(), amount.split("=")[0]);

        if (!result)
            return "didn't work";

        return "done";
    }

    // 유저가 계에 토큰 납입
    @PutMapping("/user-token")
    public String sendTokenUserToGye(@RequestBody SendTokenDto sendRequest) {


        int[][] table = gyeService.nonInterest(sendRequest.getTargetMoney(),sendRequest.getTotalMember());

        int month = 1;

        UserInfo userInfo = userinfoService.getUserInfo(sendRequest.getUserEmail());

        int turn =gyeService.findTurnByUserInfo_idAndGye_id(userInfo.getId(), sendRequest.getGyeId());

        boolean result = tokenService.sendTokenToGye(sendRequest.getUserEmail(),
                sendRequest.getGyeId(),  Integer.toString(table[turn][month]));

        if (!result)
            return "didn't work";

        return "done";
    }

    // 유저 토큰 수령
    @PutMapping("/gye-token")
    public String sendTokenGyeToUser(@RequestBody SendTokenDto sendRequest) {


        boolean result = tokenService.sendTokenToUser(sendRequest.getGyeId(),
                sendRequest.getUserEmail(), sendRequest.getAmount());

        if (!result)
            return "didn't work";

        return "done";
    }

    @GetMapping("/use/{email}/{amount}")
    public String useToken(@PathVariable String email,
                           @PathVariable String amount) {

        boolean result = tokenService.burnToken(email, amount);

        if (!result)
            return "didn't work";

        return "done";
    }

}