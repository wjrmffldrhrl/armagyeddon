package com.blockchain.armagyeddon.controller;

import antlr.ASTNULLType;
import com.blockchain.armagyeddon.domain.dto.CreateGyeDto;
import com.blockchain.armagyeddon.domain.dto.GyeDtoNoPublicKey;
import com.blockchain.armagyeddon.domain.dto.UserInfoDto;
import com.blockchain.armagyeddon.domain.dto.UserInfoDtoNoPassword;
import com.blockchain.armagyeddon.domain.entity.Gye;
import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import com.blockchain.armagyeddon.domain.repository.MemberRepository;
import com.blockchain.armagyeddon.service.GyeService;
import com.blockchain.armagyeddon.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.SetOfIntegerSyntax;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final GyeService gyeService;

    // 회원 가입
    @PostMapping("/user-info")
    public String saveUserInfo(@RequestBody UserInfoDto userInfoDto) {
        userInfoService.saveUserInfo(userInfoDto);
        return "save user " + userInfoDto.getName();
    }

    @GetMapping("/user-info/{email}")
    public UserInfoDtoNoPassword getUserInfo(@PathVariable("email") String email) {
        UserInfo userInfo = userInfoService.getUserInfo(email);

        return UserInfoDtoNoPassword.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName()).build();
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already exists")
    public static class AlreadyExistsException extends RuntimeException {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

    // myPage에서 참여중인 gye 내역
    @GetMapping("/user-info/mypage/{email}")
    public ResponseEntity<List> myPage(@PathVariable("email") String email) {

        Long userId = userInfoService.getUserInfo(email).getId();

        List<String> myGyeList = new ArrayList<>();

        for (Member res : gyeService.findGyeIdListByUserId(userId)) {
            myGyeList.add(res.getGye().getTitle());
        }

        return ResponseEntity.ok(myGyeList);

    }
}