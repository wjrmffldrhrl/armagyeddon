package com.blockchain.armagyeddon.domain.repository;

import com.blockchain.armagyeddon.domain.entity.Member;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MemberRepository extends JpaRepository<Member, UserInfo> {

    boolean existsByUserInfo_idAndGye_id(Long user,Long gye);

    boolean existsByTurnAndGye_id(int turn,Long gye);

    int countByGye_id(Long id);

    int findTurnByUserInfo_idAndGye_id(Long user, Long gye);

    List<Member> findByUserInfo_id(Long user);

}