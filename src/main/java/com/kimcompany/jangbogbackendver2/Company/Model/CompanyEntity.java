package com.kimcompany.jangbogbackendver2.Company.Model;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "COMPANY",indexes = {@Index(name = "ADMIN_ID", columnList = "ADMIN_ID"),@Index(name = "COMPANY_NUM", columnList = "COMPANY_NUM")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID",unique = true)
    private Long id;

    @Column(name = "COMPANY_NUM",unique = true,nullable = false,length = 30)
    private String companyNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID",referencedColumnName = "ADMIN_ID")
    private MemberEntity memberEntity;

    @Column(name = "COMPANY_NUM_NAME",nullable = false,length = 10)
    private String name;

    @Column(name = "COMPANY_NUM_OPEN_DATE",nullable = false,length = 20)
    private String openDate;

    @Embedded
    private CommonColumn commonColumn;
}
