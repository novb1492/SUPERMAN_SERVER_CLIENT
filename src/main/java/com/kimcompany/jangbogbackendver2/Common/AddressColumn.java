package com.kimcompany.jangbogbackendver2.Common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressColumn {

    @Column(name = "POST_CODE",length = 10,nullable = false)
    private String postCode;

    @Column(name = "ADDRESS",length = 50,nullable = false)
    private String address;

    @Column(name = "DETAIL_ADDRESS",length = 50,nullable = false)
    private String detailAddress;

    public static AddressColumn set(String postCode,String address,String detailAddress){
        return AddressColumn.builder().postCode(postCode).address(address).detailAddress(detailAddress).build();
    }

}
