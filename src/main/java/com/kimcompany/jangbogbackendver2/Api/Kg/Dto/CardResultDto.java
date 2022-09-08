package com.kimcompany.jangbogbackendver2.Api.Kg.Dto;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CommonPaymentEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;

@NoArgsConstructor
@Data
@Slf4j
public class CardResultDto {
    private String P_TID;
    private String P_CARD_ISSUER_CODE;
    private String P_CARD_MEMBER_NUM;
    private String P_CARD_PURCHASE_CODE;
    private String P_CARD_PRTC_CODE;
    private String P_CARD_INTEREST;
    private String CARD_CorpFlag;
    private String P_CARD_CHECKFLAG;
    private String P_RMESG2;
    private String P_FN_CD1;
    private String P_FN_NM;
    private String P_CARD_NUM;
    private String P_AUTH_NO;
    private String P_ISP_CARDCODE;
    private String P_COUPONFLAG;
    private String P_COUPON_DISCOUNT;
    private String P_CARD_APPLPRICE;
    private String P_CARD_COUPON_PRICE;
    private String P_SRC_CODE;
    private String P_CARD_USEPOINT;
    private String NAVERPOINT_UseFreePoint;
    private String NAVERPOINT_CSHRApplYN;
    private String NAVERPOINT_CSHRApplAmt;
    private String PCO_OrderNo;
    private String CARD_EmpPrtnCode;
    private String CARD_NomlMobPrtnCode;
    private String pAmt;
    private String pType;
    private String pMid;
    private String pAuthDt;
    private String pUserName;
    private String pUserEmail;
    private Long pUserId;
    private String P_OID;

    public static CardEntity dtoToEntity(CardResultDto cardResultDto){

        return CardEntity.builder().CARD_CorpFlag(cardResultDto.getCARD_CorpFlag())
                .CARD_EmpPrtnCode(cardResultDto.getCARD_EmpPrtnCode())
                .CARD_NomlMobPrtnCode(cardResultDto.getCARD_NomlMobPrtnCode())
                .commonColumn(CommonColumn.builder().state(trueStateNum).build())
                .commonPaymentEntity(CommonPaymentEntity.builder().pAmt(cardResultDto.getPAmt()).pAuthDt(cardResultDto.getPAuthDt())
                        .pMid(cardResultDto.pMid).pOid(cardResultDto.P_OID).prtcCnt(0).prtcRemains(0)
                        .pTid(cardResultDto.getP_TID())
                        .pType(cardResultDto.getPType())
                        .pUserEmail(cardResultDto.getPUserEmail())
                        .pUserId("test")
                        .pUserName("fdsfs")
                        .prtcRemains(Integer.parseInt(cardResultDto.getP_CARD_APPLPRICE()))
                        .storeEntity(StoreEntity.builder().id(1L).build()).build()).NAVERPOINT_CSHRApplAmt(cardResultDto.getNAVERPOINT_CSHRApplAmt())
                .NAVERPOINT_CSHRApplYN(cardResultDto.getNAVERPOINT_CSHRApplYN())
                .P_CARD_APPLPRICE(cardResultDto.getP_CARD_APPLPRICE())
                .P_CARD_CHECKFLAG(cardResultDto.getP_CARD_CHECKFLAG())
                .NAVERPOINT_UseFreePoint(cardResultDto.getNAVERPOINT_UseFreePoint())
                .P_CARD_COUPON_PRICE(cardResultDto.getP_CARD_COUPON_PRICE())
                .P_CARD_INTEREST(cardResultDto.getP_CARD_INTEREST())
                .P_CARD_ISSUER_CODE(cardResultDto.getP_CARD_ISSUER_CODE())
                .P_CARD_NUM(cardResultDto.getP_CARD_NUM())
                .P_CARD_MEMBER_NUM(cardResultDto.getP_CARD_NUM())
                .P_CARD_PRTC_CODE(cardResultDto.getP_CARD_PRTC_CODE())
                .P_CARD_PURCHASE_CODE(cardResultDto.getP_CARD_PURCHASE_CODE())
                .P_CARD_USEPOINT(cardResultDto.getP_CARD_USEPOINT())
                .P_AUTH_NO(cardResultDto.getP_AUTH_NO())
                .P_COUPON_DISCOUNT(cardResultDto.getP_COUPON_DISCOUNT())
                .P_COUPONFLAG(cardResultDto.getP_COUPONFLAG())
                .P_FN_CD1(cardResultDto.getP_FN_CD1())
                .P_FN_NM(cardResultDto.getP_FN_NM())
                .P_ISP_CARDCODE(cardResultDto.getP_ISP_CARDCODE())
                .P_RMESG2(cardResultDto.getP_RMESG2())
                .CARD_NomlMobPrtnCode(cardResultDto.getCARD_NomlMobPrtnCode())
                .P_SRC_CODE(cardResultDto.getP_SRC_CODE())
                .PCO_OrderNo(cardResultDto.getPCO_OrderNo())
                .P_SRC_CODE(cardResultDto.getP_SRC_CODE())
                .PCO_OrderNo(cardResultDto.getPCO_OrderNo())
                .P_RMESG2(cardResultDto.getP_RMESG2())
                .build();
    }
    /**
     * 결제 결과 dto변환
     * 어자피 클라이언트 구축때 사용 예정이여서 미리구축
     * @param values
     * @return
     */
    public static CardResultDto set(String[] values) {
        CardResultDto cardResultDto = new CardResultDto();
        for (String value : values) {
            if (value.contains("P_TID")) {
                cardResultDto.setP_TID(value.split("=")[1]);
            } else if (value.contains("P_AUTH_DT")) {
                cardResultDto.setPAuthDt(value.split("=")[1]);
            } else if (value.contains("P_AUTH_NO")) {
                cardResultDto.setP_AUTH_NO(value.split("=")[1]);
            } else if (value.contains("P_FN_CD1")) {
                cardResultDto.setP_FN_CD1(value.split("=")[1]);
            } else if (value.contains("P_AMT")) {
                cardResultDto.setPAmt(value.split("=")[1]);
            } else if (value.contains("P_UNAME")) {
                cardResultDto.setPUserName(value.split("=")[1]);
            } else if (value.contains("P_MID")) {
                cardResultDto.setPMid(value.split("=")[1]);
            } else if (value.contains("P_OID")) {
                cardResultDto.setP_OID(value.split("=")[1]);
            } else if (value.contains("P_CARD_NUM")) {
                cardResultDto.setP_CARD_NUM(value.split("=")[1]);
            } else if (value.contains("P_CARD_ISSUER_CODE")) {
                cardResultDto.setP_CARD_ISSUER_CODE(value.split("=")[1]);
            } else if (value.contains("P_CARD_PURCHASE_CODE")) {
                cardResultDto.setP_CARD_PURCHASE_CODE(value.split("=")[1]);
            } else if (value.contains("P_CARD_PRTC_CODE")) {
                cardResultDto.setP_CARD_PRTC_CODE(value.split("=")[1]);
            } else if (value.contains("P_CARD_INTEREST")) {
                cardResultDto.setP_CARD_INTEREST(value.split("=")[1]);
            } else if (value.contains("P_CARD_CHECKFLAG")) {
                cardResultDto.setP_CARD_CHECKFLAG(value.split("=")[1]);
            }else if (value.contains("P_CARD_ISSUER_NAME")) {
                cardResultDto.setP_CARD_ISSUER_CODE(value.split("=")[1]);
            }else if(value.contains("P_FN_NM")){
                cardResultDto.setP_FN_NM(value.split("=")[1]);
            }else if(value.contains("CARD_CorpFlag")){
                cardResultDto.setCARD_CorpFlag(value.split("=")[1]);
            }else if(value.contains("P_SRC_CODE")){
                cardResultDto.setP_SRC_CODE(value.split("=")[1]);
            }else if(value.contains("P_CARD_APPLPRICE")){
                cardResultDto.setP_CARD_APPLPRICE(value.split("=")[1]);
            }
                log.info("val:{}", value);
            }
            return cardResultDto;
        }
    }
