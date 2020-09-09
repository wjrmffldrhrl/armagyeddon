package com.blockchain.armagyeddon.service;

import com.blockchain.armagyeddon.controller.UserInfoController;
import com.blockchain.armagyeddon.domain.dto.UserInfoDto;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import com.blockchain.armagyeddon.domain.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Long saveUserInfo(UserInfoDto userInfoDto) {


        boolean isExist = userInfoRepository.existsByEmail(userInfoDto.getEmail());
        if (isExist) {
            throw new UserInfoController.AlreadyExistsException("change_email");
        }

        String password = passwordEncoder.encode(userInfoDto.getPassword());
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


        return userInfoRepository.save(UserInfo.builder()
                .email(userInfoDto.getEmail())
                .password(password)
                .name(userInfoDto.getName())
                .publicKey(publicKey).build()).getId();
    }

    public UserInfo getUserInfo(String email) {

        return userInfoRepository.findByEmail(email);
    }

    public UserInfo getUserInfo(Long id) {

        return userInfoRepository.findById(id).get();
    }

}