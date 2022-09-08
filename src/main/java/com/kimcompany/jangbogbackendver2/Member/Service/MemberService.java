package com.kimcompany.jangbogbackendver2.Member.Service;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Member.Repo.MemberRepo;
import com.kimcompany.jangbogbackendver2.Member.SelectRegiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepo memberRepo;
    private final MemberSelectService memberSelectService;

    public Integer updateSuccessLogin(Long memberId){
        System.out.println( LocalDateTime.now());
        return memberRepo.updatePwdFail(0, memberId, LocalDateTime.now());
    }
    public Integer updateFailLoginTime(String userId){
        return memberRepo.updateFailNum(userId);
    }

    public SelectRegiDto selectForRegi(String userId){
        MemberEntity memberEntity=memberSelectService.selectByUserId(userId).orElseThrow(()->new IllegalArgumentException("해당 종업원을 찾을 수 없습니다"));
        return SelectRegiDto.entityToDto(memberEntity);
    }
}
