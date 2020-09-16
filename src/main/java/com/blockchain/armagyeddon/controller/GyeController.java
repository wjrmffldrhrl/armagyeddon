package com.blockchain.armagyeddon.controller;

import com.blockchain.armagyeddon.domain.dto.CreateGyeDto;
import com.blockchain.armagyeddon.domain.dto.GyeDtoNoPublicKey;
import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.dto.JoinGyeDto;
import com.blockchain.armagyeddon.domain.dto.UserInfoDtoNoPassword;
import com.blockchain.armagyeddon.domain.entity.Gye;
import com.blockchain.armagyeddon.service.GyeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GyeController {


    private final GyeService gyeService;

    // 계 생성
    @PostMapping("/gye")
    public Long saveGye(@RequestBody CreateGyeDto createGyeDto, Principal userInfo) {
        System.out.println("Controller response : " + createGyeDto.getTitle());
        createGyeDto.setMaster(userInfo.getName());
        return gyeService.save(createGyeDto);

    }

    // 계 정보 전체 조회
    @GetMapping("/gye")
    public ResponseEntity<List> findGye() {

        List<GyeDtoNoPublicKey> gyeDtoList = new ArrayList<>();

        for (Gye gye : gyeService.findAll()) {
            List<UserInfoDtoNoPassword> userInfoDto = new ArrayList<>();
            for (Member member : gye.getMembers()) {
                UserInfoDtoNoPassword dto = UserInfoDtoNoPassword.builder()
                        .email(member.getUserInfo().getEmail())
                        .name(member.getUserInfo().getName()).build();
                userInfoDto.add(dto);
            }

            gyeDtoList.add(GyeDtoNoPublicKey.builder()
                    .id(gye.getId())
                    .type(gye.getType())
                    .title(gye.getTitle())
                    .targetMoney(gye.getTargetMoney())
                    .period(gye.getPeriod())
                    .totalMember(gye.getTotalMember())
                    .state(gye.getState())
                    .master(gye.getMaster())
                    .members(userInfoDto).build());
        }


        return ResponseEntity.ok(gyeDtoList);
    }

    // 계 id로 조회
    @GetMapping("/gye/{id}")
    public ResponseEntity<GyeDtoNoPublicKey> findGye(@PathVariable Long id) {
        Gye gye = gyeService.findById(id);

        List<UserInfoDtoNoPassword> userInfoDto = new ArrayList<>();

        for (Member member : gye.getMembers()) {
            UserInfoDtoNoPassword dto = UserInfoDtoNoPassword.builder()
                    .email(member.getUserInfo().getEmail())
                    .name(member.getUserInfo().getName()).build();
            userInfoDto.add(dto);
        }

        return ResponseEntity.ok(GyeDtoNoPublicKey.builder()
                .id(gye.getId())
                .type(gye.getType())
                .title(gye.getTitle())
                .targetMoney(gye.getTargetMoney())
                .period(gye.getPeriod())
                .totalMember(gye.getTotalMember())
                .state(gye.getState())
                .master(gye.getMaster())
                .members(userInfoDto)
                .build());

    }

    // keyword로 gye 조회
    @GetMapping("/gye/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<GyeDtoNoPublicKey> gyeDtoNoPublicKeyList = gyeService.searchGye(keyword);
        model.addAttribute("gyeList", gyeDtoNoPublicKeyList);

        return "gye/list.html"; // FE단에서 처리
    }

    @PostMapping("/member")
    public String joinMember(@RequestBody JoinGyeDto joinGyeDto, Principal currentUser) {


        gyeService.saveMember(joinGyeDto.getGyeId(), currentUser.getName(), joinGyeDto.getTurn());

        return "good!";
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already exists")
    public static class AlreadyExistsException extends RuntimeException {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

}
