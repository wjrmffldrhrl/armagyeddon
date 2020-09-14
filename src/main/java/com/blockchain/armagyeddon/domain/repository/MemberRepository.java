package com.blockchain.armagyeddon.domain.repository;

import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, UserInfo> {

    boolean existsByUserInfo_idAndGye_id(Long user,Long gye);

    boolean existsByTurnAndGye_id(int turn,Long gye);

   int countByGye_id(Long id);

}