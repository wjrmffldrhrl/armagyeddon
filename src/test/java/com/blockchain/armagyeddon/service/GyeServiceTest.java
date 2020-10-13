package com.blockchain.armagyeddon.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GyeServiceTest {

    @Autowired
    private GyeService gyeService;

    @Test
    public void applyInterestTest() {


        for(int[] i : gyeService.applyInterest(120, 3, 3)) {
            for(int j : i)
                System.out.print(j + " ");

            System.out.println();
        }
    }

    @Test
    public void nonInterestTest() {


        for(int[] i : gyeService.nonInterest(120, 3)) {
            for(int j : i)
                System.out.print(j + " ");

            System.out.println();
        }
    }
}
