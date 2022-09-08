package com.kimcompany.jangbogbackendver2.Deliver.Dto;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;

@NoArgsConstructor
@Data
public class TryInsertDto {
    @NotBlank(message = "매장정보 유실")
    private String storeId;

    @Size(min = 1,message = "배달방을 만들 주문을 선택해주세요")
    private List<Long>cardIds;

    public static DeliverEntity dtoToEntity(TryInsertDto tryInsertDto){
        return DeliverEntity.builder().memberEntity(MemberEntity.builder().id(UtilService.getLoginUserId()).build())
                .commonColumn(CommonColumn.builder().state(trueStateNum).build())
                .storeEntity(StoreEntity.builder().id(Long.parseLong(tryInsertDto.getStoreId())).build())
                .build();
    }
}
