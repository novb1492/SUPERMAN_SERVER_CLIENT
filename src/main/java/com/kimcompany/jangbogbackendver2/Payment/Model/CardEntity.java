package com.kimcompany.jangbogbackendver2.Payment.Model;


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
@Table(name = "CARD")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CardEntity {

    @Id
    @Column(name = "CARD_ID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "P_CARD_ISSUER_CODE")
    private String P_CARD_ISSUER_CODE;

    @Column(name = "P_CARD_MEMBER_NUM")
    private String P_CARD_MEMBER_NUM;

    @Column(name = "P_CARD_PURCHASE_CODE")
    private String P_CARD_PURCHASE_CODE;

    @Column(name = "P_CARD_PRTC_CODE")
    private String P_CARD_PRTC_CODE;

    @Column(name = "P_CARD_INTEREST")
    private String P_CARD_INTEREST;

    @Column(name = "CARD_CorpFlag")
    private String CARD_CorpFlag;

    @Column(name = "P_CARD_CHECKFLAG")
    private String P_CARD_CHECKFLAG;

    @Column(name = "P_RMESG2",length = 500)
    private String P_RMESG2;

    @Column(name = "P_FN_CD1")
    private String P_FN_CD1;

    @Column(name = "P_FN_NM")
    private String P_FN_NM;

    @Column(name = "P_CARD_NUM")
    private String P_CARD_NUM;

    @Column(name = "P_AUTH_NO")
    private String P_AUTH_NO;

    @Column(name = "P_ISP_CARDCODE")
    private String P_ISP_CARDCODE;

    @Column(name = "P_COUPONFLAG")
    private String P_COUPONFLAG;

    @Column(name = "P_COUPON_DISCOUNT")
    private String P_COUPON_DISCOUNT;

    @Column(name = "P_CARD_APPLPRICE")
    private String P_CARD_APPLPRICE;

    @Column(name = "P_CARD_COUPON_PRICE")
    private String P_CARD_COUPON_PRICE;

    @Column(name = "P_SRC_CODE")
    private String P_SRC_CODE;

    @Column(name = "P_CARD_USEPOINT")
    private String P_CARD_USEPOINT;

    @Column(name = "NAVERPOINT_UseFreePoint")
    private String NAVERPOINT_UseFreePoint;

    @Column(name = "NAVERPOINT_CSHRApplYN")
    private String NAVERPOINT_CSHRApplYN;

    @Column(name = "NAVERPOINT_CSHRApplAmt")
    private String NAVERPOINT_CSHRApplAmt;

    @Column(name = "PCO_OrderNo")
    private String PCO_OrderNo;

    @Column(name = "CARD_EmpPrtnCode")
    private String CARD_EmpPrtnCode;

    @Column(name = "CARD_NomlMobPrtnCode")
    private String CARD_NomlMobPrtnCode;

    @Column(name = "CARD_prtcRemains")
    private String prtcRemains;

    @Embedded
    private CommonPaymentEntity commonPaymentEntity;

    @Embedded
    private CommonColumn commonColumn;
}
