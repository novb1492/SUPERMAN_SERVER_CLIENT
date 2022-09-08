package com.kimcompany.jangbogbackendver2.Member;

import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SelectRegiDto {

    private String userId;
    private String firstName;
    private String lastName;
    private String role;
    private long id;

    public static SelectRegiDto entityToDto(MemberEntity memberEntity){
        SelectRegiDto selectDto = new SelectRegiDto();
        selectDto.setUserId(memberEntity.getUserId());
        selectDto.setFirstName(memberEntity.getFirstName());
        selectDto.setLastName(memberEntity.getLastName());
        selectDto.setRole(memberEntity.getRole());
        selectDto.setId(memberEntity.getId());
        return selectDto;
    }

}
