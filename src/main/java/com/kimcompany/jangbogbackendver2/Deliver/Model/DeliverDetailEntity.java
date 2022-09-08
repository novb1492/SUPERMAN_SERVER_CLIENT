package com.kimcompany.jangbogbackendver2.Deliver.Model;

import com.kimcompany.jangbogbackendver2.Common.CommonColumn;
import com.kimcompany.jangbogbackendver2.Member.Model.ClientEntity;
import com.kimcompany.jangbogbackendver2.Member.Model.MemberEntity;
import com.kimcompany.jangbogbackendver2.Payment.Model.CardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "DELIVER_DETAIL",indexes = {@Index(name = "DELIVER_ID_INDEX",columnList = "DELIVER_DETAIL_DELIVER_ID"),
        @Index(name = "CARD_ID_INDEX",columnList = "DELIVER_DETAIL_CARD_ID")})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class DeliverDetailEntity {

    @Id
    @Column(name = "DELIVER_DETAIL_ID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVER_DETAIL_CLIENT_ID",referencedColumnName = "CLIENT_ID",nullable = false)
    private ClientEntity clientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVER_DETAIL_CARD_ID",referencedColumnName = "CARD_ID",nullable = false)
    private CardEntity cardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVER_DETAIL_ADMIN_ID", referencedColumnName = "ADMIN_ID", nullable = true)
    private MemberEntity deliverAdminEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVER_DETAIL_DELIVER_ID",referencedColumnName = "DELIVER_ID",nullable = false)
    private DeliverEntity deliverEntity;

    @Embedded
    private CommonColumn commonColumn;
}
