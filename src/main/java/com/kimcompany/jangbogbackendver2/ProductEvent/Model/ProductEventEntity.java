package com.kimcompany.jangbogbackendver2.ProductEvent.Model;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "PRODUCT_EVENT")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ProductEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_EVENT_ID", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID",referencedColumnName = "PRODUCT_ID")
    private ProductEntity productEntity;

    @Column(name = "EVENT_PRICE",nullable = false,length = 20)
    private String eventPrice;

    @Column(name = "START_DATE",nullable = false)
    private Timestamp startDate;

    @Column(name = "END_DATE",nullable = false)
    private Timestamp endDate;

    @Column(name = "PRODUCT_EVENT_NAME",nullable = false,length = 20)
    private String name;

    @Embedded
    private CommonColumn commonColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSERT_USER",referencedColumnName = "ADMIN_ID")
    private MemberEntity memberEntity;

}
