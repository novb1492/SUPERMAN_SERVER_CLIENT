package com.kimcompany.jangbogbackendver2.Member.Model;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "ADMIN",indexes = {@Index(name = "EMAIL_INDEX", columnList = "EMAIL"),@Index(name = "PHONE_INDEX", columnList = "PHONE"),@Index(name = "ID_INDEX", columnList = "ID")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_ID",unique = true)
    private Long id;

    @Column(name = "ID",nullable = false,length = 50)
    private String userId;
    @Column(name = "EMAIL",nullable = false,length = 50)
    private String email;

    @Column(name = "PWD",nullable = false,length = 1000)
    private String pwd;

    @Column(name = "FIRST_NAME",nullable = false,length = 20)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false,length = 20)
    private String lastName;

    @Column(name = "PHONE",nullable = false,length = 20)
    private String phone;

    @Column(name = "ROLE",nullable = false,length = 20)
    private String role;

    @Embedded
    private CommonColumn commonColumn;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;

    @Column(name = "Last_UPDATE_PWD_DATE",nullable = false)
    private LocalDate lastUpdatePwdDate;

    @Column(name = "FAIL_PWD",nullable = false,columnDefinition = "TINYINT")
    private Integer failPwd;

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", commonColumn=" + commonColumn +
                ", lastLoginDate=" + lastLoginDate +
                ", lastUpdatePwdDate=" + lastUpdatePwdDate +
                '}';
    }
}
