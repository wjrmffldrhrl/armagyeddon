package com.blockchain.armagyeddon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
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

    private LocalDateTime payDay;

    private int totalMember;

    private String state;

    private String master;

    private List<UserInfoDtoNoPassword> members = new ArrayList<>();

}
