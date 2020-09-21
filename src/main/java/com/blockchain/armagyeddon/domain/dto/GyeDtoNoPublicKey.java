package com.blockchain.armagyeddon.domain.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GyeDtoNoPublicKey {

    private Long id;

    private String interest;

    private String type;

    private String title;

    private int targetMoney;

    private int period;

    private int totalMember;

    private String state;

    private String master;

    private List<UserInfoDtoNoPassword> members = new ArrayList<>();

}
