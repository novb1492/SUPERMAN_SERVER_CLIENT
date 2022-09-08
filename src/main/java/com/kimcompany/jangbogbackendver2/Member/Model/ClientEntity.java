package com.kimcompany.jangbogbackendver2.Member.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "CLIENT",indexes = {@Index(name = "EMAIL_INDEX", columnList = "CLIENT_EMAIL"),@Index(name = "PHONE_INDEX", columnList = "CLIENT_PHONE")})
@Entity
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID",unique = true)
    private Long id;

    @Column(name = "CLIENT_EMAIL",nullable = false,length = 50)
    private String email;

    @Column(name = "PWD",nullable = false,length = 1000)
    private String pwd;

    @Column(name = "CLIENT_FIRST_NAME",nullable = false,length = 20)
    private String firstName;

    @Column(name = "CLIENT_LAST_NAME",nullable = false,length = 20)
    private String lastName;

    @Column(name = "CLIENT_FULL_NAME",nullable = false,length = 40)
    private String fullName;

    @Column(name = "CLIENT_PHONE",nullable = false,length = 20)
    private String phone;
}
