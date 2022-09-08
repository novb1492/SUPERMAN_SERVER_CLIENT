package com.kimcompany.jangbogbackendver2.Member.Repo;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepo extends JpaRepository<MemberEntity,Long>, MemberSupport {

    @Query("SELECT m from MemberEntity  m where m.userId=:userId and m.commonColumn.state<>:state")
    Optional<MemberEntity> findByUserId(@Param("userId") String userId,@Param("state")int state);

    @Modifying
    @Transactional
    @Query("UPDATE MemberEntity m SET m.lastLoginDate=:now WHERE m.id=:id")
    Integer updateLoginDate(@Param("now")LocalDateTime now ,@Param("id")Long id);

    @Modifying
    @Query("update MemberEntity m set m.failPwd=:num,m.lastLoginDate=:now where m.id=:id")
    Integer updatePwdFail(@Param("num")int num,@Param("id")Long id,@Param(("now"))LocalDateTime now);

    @Modifying
    @Query("update MemberEntity m set m.failPwd=m.failPwd+1 where m.userId=:userId")
    Integer updateFailNum(@Param("userId")String userId);


}
