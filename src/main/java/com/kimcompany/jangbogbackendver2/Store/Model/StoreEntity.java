package com.kimcompany.jangbogbackendver2.Store.Model;

import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Company.Model.CompanyEntity;
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
@Table(name = "STORE",indexes = {@Index(name = "NAME", columnList = "NAME")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID",referencedColumnName = "ADMIN_ID",nullable = false)
    private MemberEntity memberEntity;

    @Column(name = "MIN_ORDER_PRICE",nullable = false)
    private Integer minOrderPrice;

    @Column(name = "MAX_DELIVER_RADIUS",nullable = false)
    private Double maxDeliverRadius;

    @Column(name = "OPEN_TIME",nullable = false,length = 6)
    private String openTime;

    @Column(name = "CLOSE_TIME",nullable = false,length = 6)
    private String closeTime;

    @Column(name = "TEL",nullable = false,length = 20)
    private String tel;

    @Lob
    @Column(name = "TEXT",length = 3500)
    private String text;

    @Column(name = "NAME",nullable = false,length = 20)
    private String name;

    @Column(name = "STORE_THUMBNAIL",nullable = false,length = 500)
    private String thumbNail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_COMPANY_NUM",referencedColumnName = "COMPANY_ID",nullable = false)
    private CompanyEntity companyEntity;

    @Embedded
    private CommonColumn commonColumn;

    @Embedded
    private AddressColumn addressColumn;




}
