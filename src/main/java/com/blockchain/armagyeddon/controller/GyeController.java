package com.blockchain.armagyeddon.controller;

import com.blockchain.armagyeddon.domain.dto.CreateGyeDto;
import com.blockchain.armagyeddon.domain.dto.GyeDtoNoPublicKey;
import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.dto.JoinGyeDto;
import com.blockchain.armagyeddon.domain.dto.UserInfoDtoNoPassword;
import com.blockchain.armagyeddon.domain.entity.Gye;
import com.blockchain.armagyeddon.domain.repository.GyeRepository;
import com.blockchain.armagyeddon.service.GyeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GyeController {


    private final GyeService gyeService;
    private final GyeRepository gyeRepository;

    // 계 생성
    @PostMapping("/gye")
    public Long saveGye(@RequestBody CreateGyeDto createGyeDto, Principal userInfo) {
        System.out.println("Controller response : " + createGyeDto.getTitle());
        createGyeDto.setMaster(userInfo.getName());
        return gyeService.save(createGyeDto);

    }

    // 계 전체 조회
    @GetMapping("/gye")
    public ResponseEntity<List> findGye() {

        List<GyeDtoNoPublicKey> gyeDtoList = new ArrayList<>();

        for (Gye gye : gyeService.findAll()) {
            List<UserInfoDtoNoPassword> userInfoDto = new ArrayList<>();
            for (Member member : gye.getMembers()) {
                UserInfoDtoNoPassword dto = UserInfoDtoNoPassword.builder()
                        .email(member.getUserInfo().getEmail())
                        .name(member.getUserInfo().getName())
                        .turn(member.getTurn()).build();
                userInfoDto.add(dto);
            }

            gyeDtoList.add(GyeDtoNoPublicKey.builder()
                    .id(gye.getId())
                    .interest(gye.getInterest())
                    .type(gye.getType())
                    .title(gye.getTitle())
                    .targetMoney(gye.getTargetMoney())
                    .period(gye.getPeriod())
                    .payDay(gye.getPayDay())
                    .totalMember(gye.getTotalMember())
                    .state(gye.getState())
                    .master(gye.getMaster())
                    .members(userInfoDto).build());
        }


        return ResponseEntity.ok(gyeDtoList);
    }

    // 유효한 계 조회
    @GetMapping("/validate-gye")
    public ResponseEntity<List> findValidateGye() {

        List<GyeDtoNoPublicKey> gyeDtoList = new ArrayList<>();

        for (Gye gye : gyeService.findValidateGye()) {

            List<UserInfoDtoNoPassword> userInfoDto = new ArrayList<>();

            for (Member member : gye.getMembers()) {
                UserInfoDtoNoPassword dto = UserInfoDtoNoPassword.builder()
                        .email(member.getUserInfo().getEmail())
                        .name(member.getUserInfo().getName())
                        .turn(member.getTurn()).build();
                userInfoDto.add(dto);
            }

            gyeDtoList.add(GyeDtoNoPublicKey.builder()
                    .id(gye.getId())
                    .interest(gye.getInterest())
                    .type(gye.getType())
                    .title(gye.getTitle())
                    .targetMoney(gye.getTargetMoney())
                    .period(gye.getPeriod())
                    .payDay(gye.getPayDay())
                    .totalMember(gye.getTotalMember())
                    .state(gye.getState())
                    .master(gye.getMaster())
                    .members(userInfoDto).build());
        }


        return ResponseEntity.ok(gyeDtoList);
    }

    // keyword로 계 조회
    @GetMapping("/gye/search/{keyword}")
    public ResponseEntity<List> search(@PathVariable String keyword) {

        List<GyeDtoNoPublicKey> gyeDtoList = new ArrayList<>();

        for (Gye gye : gyeService.search(keyword)) {
            List<UserInfoDtoNoPassword> userInfoDto = new ArrayList<>();

            for (Member member : gye.getMembers()) {
                UserInfoDtoNoPassword dto = UserInfoDtoNoPassword.builder()
                        .email(member.getUserInfo().getEmail())
                        .name(member.getUserInfo().getName()).build();
                userInfoDto.add(dto);
            }

            gyeDtoList.add(GyeDtoNoPublicKey.builder()
                    .id(gye.getId())
                    .interest(gye.getInterest())
                    .type(gye.getType())
                    .title(gye.getTitle())
                    .targetMoney(gye.getTargetMoney())
                    .period(gye.getPeriod())
                    .payDay(gye.getPayDay())
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
                    .name(member.getUserInfo().getName())
                    .turn(member.getTurn()).build();
            userInfoDto.add(dto);
        }

        return ResponseEntity.ok(GyeDtoNoPublicKey.builder()
                .id(gye.getId())
                .interest(gye.getInterest())
                .type(gye.getType())
                .title(gye.getTitle())
                .targetMoney(gye.getTargetMoney())
                .period(gye.getPeriod())
                .payDay(gye.getPayDay())
                .totalMember(gye.getTotalMember())
                .state(gye.getState())
                .master(gye.getMaster())
                .members(userInfoDto)
                .build());
    }

    // 계 참여
    @PostMapping("/member")
    public String joinMember(@RequestBody JoinGyeDto joinGyeDto, Principal currentUser) {

        gyeService.saveMember(joinGyeDto.getGyeId(), currentUser.getName(), joinGyeDto.getTurn());

        return "good!";
    }

    // 계 상태 변경
    @PostMapping("/gye-state/{id}")
    public ResponseEntity updateState(@PathVariable Long id, @RequestBody Gye updateGye) {

        Optional<Gye> gye = gyeRepository.findById(id);

        // 수정
        gye.get().setState(updateGye.getState());
        gye.get().setPayDay(updateGye.getPayDay());

        // 수정 후 저장
        gyeRepository.save(gye.get());

        System.out.println("update Gye state to " + updateGye.getState());

        return new ResponseEntity<> ("done!", HttpStatus.OK);

    }

    // 예외 처리
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already exists")
    public static class AlreadyExistsException extends RuntimeException {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

}
