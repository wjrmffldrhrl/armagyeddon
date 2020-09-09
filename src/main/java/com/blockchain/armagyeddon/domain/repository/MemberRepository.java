package com.blockchain.armagyeddon.domain.repository;

import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserInfo(Long userInfo);
}
