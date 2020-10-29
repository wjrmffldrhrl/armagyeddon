package com.blockchain.armagyeddon.domain.repository;


import com.blockchain.armagyeddon.domain.entity.Gye;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GyeRepository extends JpaRepository<Gye, Long> {

    List<Gye> findByTitleContaining(String keyword);

    List<Gye> findByStateIsNot(String state);

}