package com.kimcompany.jangbogbackendver2.Company.Dto;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Company.Model.CompanyEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.getLoginUserId;

@NoArgsConstructor
@Data
public class TryInsertDto {

    @NotBlank(message = "사업자번호를 입력해주세요")
    private String companyNum;

    @NotBlank(message = "개업일을 입력해주세요")
    private String date;

    @NotBlank(message = "대표자성명을 입력해주세요")
    private String name;

    public static CompanyEntity dtoToEntity(TryInsertDto tryInsertDto){
        return CompanyEntity.builder().companyNum(tryInsertDto.getCompanyNum())
                .memberEntity(MemberEntity.builder().id(getLoginUserId()).build())
                .name(tryInsertDto.getName()).openDate(tryInsertDto.getDate())
                .commonColumn(CommonColumn.set(trueStateNum)).companyNum(tryInsertDto.getCompanyNum())
                .build();
    }

}
