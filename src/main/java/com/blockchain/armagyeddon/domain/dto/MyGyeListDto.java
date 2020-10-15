package com.blockchain.armagyeddon.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyGyeListDto {
    private Long id;

    private String type;

    private String interest;

    private String title;

    private int targetMoney;

    private int period;

    private int totalMember;

    private String state;

    private String master;

    private int turn;
}


