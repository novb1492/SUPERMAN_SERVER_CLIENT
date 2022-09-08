package com.kimcompany.jangbogbackendver2.Common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonColumn {

    @Column(name = "STATE", columnDefinition = "TINYINT")
    private Integer state;

    @Column(name = "CREATED")
    @CreatedDate
    private LocalDateTime created;

    public static CommonColumn set(int state){
        return CommonColumn.builder().state(state).build();
    }
}
