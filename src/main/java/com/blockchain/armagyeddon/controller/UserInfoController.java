package com.blockchain.armagyeddon.controller;

import com.blockchain.armagyeddon.domain.dto.UserInfoDto;
import com.blockchain.armagyeddon.domain.dto.UserInfoDtoNoPassword;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import com.blockchain.armagyeddon.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping
public class UserInfoController {

    private final UserInfoService userInfoService;

    // 회원 가입
    @PostMapping("/user-info")
    public String saveUserInfo(@RequestBody UserInfoDto userInfoDto) {
        userInfoService.saveUserInfo(userInfoDto);
        return "save user " + userInfoDto.getName();
    }

    @GetMapping("/user-info/{email}")
    public UserInfoDtoNoPassword getUserInfo(@PathVariable("email") String email) {
        UserInfo userinfo = userInfoService.getUserInfo(email);

        return UserInfoDtoNoPassword.builder()
                .email(userinfo.getEmail())
                .name(userinfo.getName()).build();
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already exists")
    public static class AlreadyExistsException extends RuntimeException {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

    @GetMapping()

}

