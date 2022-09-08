package com.kimcompany.jangbogbackendver2.Member.Model;

import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Slf4j
public class PrincipalDetails implements UserDetails {
    private MemberEntity memberEntity;
    private String loginType;

    public PrincipalDetails(MemberEntity memberEntity){
        this.memberEntity=memberEntity;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = memberEntity.getRole();
        Collection<GrantedAuthority> roles = new ArrayList<>();
        log.info("유저의 권한: {}",role);
        roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPwd();
    }

    @Override
    public String getUsername() {
        return memberEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return memberEntity.getLastLoginDate().plusYears(BasicText.loginPeriodYear).isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if(memberEntity.getLastUpdatePwdDate().plusMonths(BasicText.updatePwdPeriodMon).isAfter(LocalDate.now())){
            return true;
        }else if(memberEntity.getFailPwd()<5){
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return memberEntity.getCommonColumn().getState() == BasicText.commonState;
    }
}
