
package com.blockchain.armagyeddon.service;


import com.blockchain.armagyeddon.controller.GyeController;
import com.blockchain.armagyeddon.domain.dto.CreateGyeDto;
import com.blockchain.armagyeddon.domain.dto.UserInfoDto;
import com.blockchain.armagyeddon.domain.entity.Gye;
import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import com.blockchain.armagyeddon.domain.repository.GyeRepository;
import com.blockchain.armagyeddon.domain.repository.MemberRepository;
import com.blockchain.armagyeddon.domain.repository.UserInfoRepository;
import com.sun.xml.bind.v2.model.core.ID;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;


import javax.transaction.Transactional;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GyeService {


    private final GyeRepository gyeRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final UserInfoRepository userInfoRepository;

    public List<Gye> findAll() {

        return gyeRepository.findAll();
    }

    public Gye findById(Long id) {

        return gyeRepository.findById(id).get();
    }

    //계 삭제
    public void deleteById(Long id) {
        gyeRepository.deleteById(id);
    }

    //계 생성
    public Long save(CreateGyeDto createGyeDto) {

        String password = passwordEncoder.encode(createGyeDto.getMaster());
        String publicKey = "";
        try {
            publicKey = tokenService.createAccount(password);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

        Long gyeId = gyeRepository.save(Gye.builder()
                .type(createGyeDto.getType())
                .title(createGyeDto.getTitle())
                .targetMoney(createGyeDto.getTargetMoney())
                .period(createGyeDto.getPeriod())
                .totalMember(createGyeDto.getTotalMember())
                .state(createGyeDto.getState())
                .master(createGyeDto.getMaster())
                .publicKey(publicKey).build()).getId();

        // 계 생성시 계주는 자동으로 계 맴버에 포함된다.
        this.saveMember(gyeId, createGyeDto.getMaster(), createGyeDto.getTurn());

        return gyeId;
    }

    // 계-회원 저장
    public Long saveMember(Long gyeId, String email, int turn) {

        Gye gye = gyeRepository.findById(gyeId).get();
        UserInfo userInfo = userInfoRepository.findByEmail(email);

        Member member = Member.builder()
                .gye(gye)
                .userInfo(userInfo)
                .userState("live")
                .turn(turn).build();

        boolean isExist = memberRepository.existsByUserInfo(userInfo);

        if (isExist) {
            throw new GyeController.AlreadyExistsException("you've already joined");
        } else {
            Member savedMember = memberRepository.save(member);

            return savedMember.getId();
        }
    }

}