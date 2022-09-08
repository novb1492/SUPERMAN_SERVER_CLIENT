package com.kimcompany.jangbogbackendver2.Product.Dto;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import com.kimcompany.jangbogbackendver2.ProductKind.Model.ProductCategoryEntity;
import com.kimcompany.jangbogbackendver2.Store.Model.StoreEntity;
import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.kimcompany.jangbogbackendver2.Text.BasicText.trueStateNum;
import static com.kimcompany.jangbogbackendver2.Util.UtilService.getLoginUserId;

@NoArgsConstructor
@Data
public class TryInsertDto {

    @NotBlank(message = "상품 카테고리를 선택해주세요")
    private String category;

    @Size(max = 30,message = "원산지는 최대 30자입니다")
    @NotBlank(message = "상품 원산지를 적어주세요")
    private String origin;

    @NotBlank(message = "상품 설명을 적어주세요")
    private String introduce;

    @NotBlank(message = "상품 이미지를 올려주세요")
    private String productImgPath;

    @Min(value = 100,message = "최소 가격은 100원입니다")
    private Integer  price;

    @Size(message = "상품이름은 최대 30자입니다")
    @NotBlank(message = "상품 이름을 적어주세요")
    private String name;

    @NotBlank(message = "매장정보를 유실했습니다")
    private String id;


    /**
     * 상품의 이벤트 내용 null가능
     */
    private List<Map<String,Object>> events;

    //가격에 , 문자 붙힌뒤 db에 저장될때 사용하는
    private String pricePLusChar;

    public static ProductEntity dtoToEntity(TryInsertDto tryInsertDto){
        return ProductEntity.builder().productKindEntity(ProductCategoryEntity.builder().id(Long.parseLong(tryInsertDto.getCategory())).build()).commonColumn(CommonColumn.set(trueStateNum))
                .introduce(tryInsertDto.getIntroduce()).name(tryInsertDto.getName()).origin(tryInsertDto.getOrigin())
                .price(tryInsertDto.getPricePLusChar()).productImgPath(tryInsertDto.getProductImgPath()).memberEntity(MemberEntity.builder().id(getLoginUserId())
                        .build()).storeEntity(StoreEntity.builder().id(Long.parseLong(tryInsertDto.getId())).build())
                .build();
    }
    public static ProductEventEntity dtoToEntity(Map<String,Object>event, long productId){
        return ProductEventEntity.builder().productEntity(ProductEntity.builder().id(productId).build())
                .endDate(Timestamp.valueOf(event.get("endDate").toString().replace("T"," ")+":00"))
                .startDate(Timestamp.valueOf(event.get("startDate").toString().replace("T"," ")+":00"))
                .commonColumn(CommonColumn.set(trueStateNum)).memberEntity(MemberEntity.builder().id(getLoginUserId()).build())
                .eventPrice(event.get("price").toString()).name(event.get("name").toString()).build();
    }

}
