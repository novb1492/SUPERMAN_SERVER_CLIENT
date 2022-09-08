package com.kimcompany.jangbogbackendver2.Employee.Dto;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Employee.Model.EmployeeEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class TryInsertDto {

    @NotBlank(message = "매장을 선택해 주세요")
    private String storeId;
    @NotBlank(message = "초대할 직원을 선택해 주세요")
    private String userId;


    public static EmployeeEntity dtoToEntity(TryInsertDto tryInsertDto){
        return EmployeeEntity.builder().memberEntity(MemberEntity.builder().id(Long.parseLong(tryInsertDto.getUserId())).build())
                .storeEntity(StoreEntity.builder().id(Long.parseLong(tryInsertDto.getStoreId())).build())
                .commonColumn(CommonColumn.set(1)).insertUser(MemberEntity.builder().id(UtilService.getLoginUserId()).build())
                .build();
    }
}
