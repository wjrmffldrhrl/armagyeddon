package com.blockchain.armagyeddon.domain.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDtoNoPassword {

    private String email;
    private String name;
    private int turn;

}
