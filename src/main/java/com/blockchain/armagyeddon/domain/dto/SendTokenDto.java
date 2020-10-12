package com.blockchain.armagyeddon.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTokenDto {
    private String userEmail;
    private Long gyeId;
    private String amount;
    private Long targetMoney;
    private Long totalMember;
    private Long interest;


}
