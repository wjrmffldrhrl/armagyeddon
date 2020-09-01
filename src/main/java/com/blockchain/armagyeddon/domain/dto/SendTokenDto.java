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

}
