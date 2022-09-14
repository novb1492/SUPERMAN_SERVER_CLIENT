package com.kimcompany.jangbogbackendver2.Cart.Model;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Model.ProductCategoryEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "CART",indexes = {@Index(name = "PRODUCT_ID",columnList = "PRODUCT_ID"),@Index(name = "CLIENT_ID",columnList = "CLIENT_ID")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID", unique = true)
    private Long id;

    @Embedded
    private CommonColumn commonColumn;

    @Column(name = "PRODUCT_PRICE",nullable = false,length = 20)
    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID",referencedColumnName = "CLIENT_ID",nullable = false)
    private ClientEntity clientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID",referencedColumnName ="PRODUCT_ID" ,nullable = false)
    private ProductEntity productEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_EVENT_ID",referencedColumnName ="PRODUCT_EVENT_ID" )
    private ProductEventEntity productEventEntities;
}
