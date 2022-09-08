package com.kimcompany.jangbogbackendver2.Order.Model;

import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
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
@Table(name = "ORDERS",indexes = {@Index(name = "ORDERS_PRODUCT_ID",columnList = "ORDERS_PRODUCT_ID")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "ORDERS_STORE_ID",referencedColumnName = "STORE_ID",nullable = false)
    private StoreEntity storeEntity;

    @Column(name = "PRICE",nullable = false)
    private Integer price;

    @Column(name = "TOTAL_COUNT",nullable = false)
    private Integer totalCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERS_EVENT_ID",referencedColumnName = "PRODUCT_EVENT_ID",nullable = true)
    private ProductEventEntity productEventEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERS_PRODUCT_ID",referencedColumnName = "PRODUCT_ID",nullable = false)
    private ProductEntity productEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERS_CLIENT_ID",referencedColumnName = "CLIENT_ID",nullable = false)
    private ClientEntity clientEntity;

    @Embedded
    private AddressColumn addressColumn;

    @Embedded
    private CommonColumn commonColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERS_CARD_ID",referencedColumnName = "CARD_ID",nullable = false)
    private CardEntity cardEntity;

}
