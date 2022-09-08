package com.kimcompany.jangbogbackendver2.Store.Dto;

import com.kimcompany.jangbogbackendver2.Common.AddressColumn;
import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Company.Model.CompanyEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class TryInsertDto {

    @NotBlank(message = "매장 썸네일을 업로드해주세요")
    private String thumbnail;
    @NotBlank(message = "매장 오픈 시간을 선택해주세요")
    private String openTime;
    @NotBlank(message = "매장 마감 시간을 선택해주세요")
    private String closeTime;
    @NotBlank(message = "우편번호를 입력해주세요")
    private String postcode;

    @Size(message = "주소는 최대 30자입니다")
    @NotBlank(message = "주소를 입력해주세요")
    private String address;

    @Size(message = "매장이름은 최대 20자입니다")
    @NotBlank(message = "매장이름을 입력해주세요")
    private String name;

    @NotBlank(message = "사업자 번호를 선택해주세요")
    private String companyNum;

    private String text;

    @Size(max = 50,message = "상세주소는 최대50자입니다")
    @NotBlank(message = "매장상세주소를 입력해주세요")
    private String detailAddress;

    @Min(value = 1,message = "최소 배달금액을 입력해주세요")
    private int minPrice;

    @NotBlank(message = "최대 배달반경을 입력해주세요")
    private String radius;

    @Size(max = 20,message = "전화번호는 최대 20자입니다")
    @NotBlank(message = "매장 전화번호를 입력해주세요")
    private String tel;

    public static StoreEntity dtoToEntity(TryInsertDto tryInsertDto){
        return StoreEntity.builder().memberEntity(MemberEntity.builder().id(UtilService.getPrincipalDetails().getMemberEntity().getId()).build())
                .addressColumn(AddressColumn.set(tryInsertDto.getPostcode(), tryInsertDto.getAddress(), tryInsertDto.getDetailAddress()))
                .closeTime(tryInsertDto.getCloseTime()).openTime(tryInsertDto.getOpenTime()).maxDeliverRadius(Double.parseDouble(tryInsertDto.getRadius()))
                .name(tryInsertDto.getName()).tel(tryInsertDto.getTel()).text(tryInsertDto.getText()).commonColumn(CommonColumn.set(1))
                .minOrderPrice(tryInsertDto.getMinPrice()).thumbNail(tryInsertDto.getThumbnail())
                .companyEntity(CompanyEntity.builder().id(Long.parseLong(tryInsertDto.getCompanyNum())).build()).build();
    }

}
