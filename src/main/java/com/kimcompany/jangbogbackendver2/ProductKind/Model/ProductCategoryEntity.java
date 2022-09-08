package com.kimcompany.jangbogbackendver2.ProductKind.Model;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
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
@Table(name = "PRODUCT_CATEGORY",indexes = {@Index(name = "PRODUCT_CATEGORY_NAME", columnList = "PRODUCT_CATEGORY_NAME")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_CATEGORY_ID")
    private Long id;

    @Column(name = "PRODUCT_CATEGORY_NAME",nullable = false,length = 12)
    private String name;

    @Embedded
    private CommonColumn commonColumn;
}
