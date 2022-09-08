package com.kimcompany.jangbogbackendver2.Payment.Model;

import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonPaymentEntity {

    @Column(name = "P_TID")
    private String pTid;

    @Column(name = "P_OID")
    private String pOid;

    @Column(name = "P_AMT")
    private String pAmt;

    @Column(name = "P_TYPE")
    private String pType;

    @Column(name = "P_MID")
    private String pMid;

    @Column(name = "P_AUTH_DT")
    private String pAuthDt;

    @Column(name = "P_USER_NAME")
    private String pUserName;

    @Column(name = "P_USER_EMAIL")
    private String pUserEmail;

    @Column(name = "P_USER_ID")
    private String pUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID",referencedColumnName = "STORE_ID")
    private StoreEntity storeEntity;

    /**
     * 취소 관련 변수들
     */
    @Column(name = "prtcCnt")
    private Integer prtcCnt;

    @Column(name = "prtcRemains")
    private Integer prtcRemains;

}
