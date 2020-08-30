package com.blockchain.armagyeddon.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTokenDto {
    private String from;
    private Long gyeId;
    private String amount;

}
