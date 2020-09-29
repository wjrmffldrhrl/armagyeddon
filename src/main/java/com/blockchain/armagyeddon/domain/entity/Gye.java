package com.blockchain.armagyeddon.domain.entity;


import javax.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Gye {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String type;

    private String interest;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private int targetMoney;

    @Column(nullable = false)
    private int period;

    @Column(nullable = false)
    private int totalMember;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String master;

    private String publicKey;




    @OneToMany(mappedBy = "gye")
    private List<Member> members = new ArrayList<>();
}

