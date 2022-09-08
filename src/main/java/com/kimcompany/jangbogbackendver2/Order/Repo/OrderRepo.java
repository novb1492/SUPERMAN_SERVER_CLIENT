package com.kimcompany.jangbogbackendver2.Order.Repo;

import com.kimcompany.jangbogbackendver2.Order.Model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderEntity,Long>,OrderRepoCustom {

    @Modifying
    @Query("update OrderEntity o set o.commonColumn.state=:state,o.totalCount=:count where o.id=:id and o.storeEntity.id=:storeId")
    Integer updateAfterRefund(@Param("state")int refundState,@Param("count")int count,@Param("id")long id,@Param("storeId")long storeId);

    @Query("select o.commonColumn.state from OrderEntity o where o.storeEntity.id=:storeId and o.cardEntity.id=:cardId and o.commonColumn.state<>:state" )
    List<Integer>findByIdsForStateCheck(@Param("cardId")long cardId,@Param("storeId")long storeId,@Param("state")int deleteState);

    @Modifying
    @Query("update OrderEntity o set o.commonColumn.state=:state,o.totalCount=:count where o.cardEntity.id=:cardId and o.storeEntity.id=:storeId")
    Integer updateAfterRefundCheck(@Param("state")int refundState,@Param("count")int count,@Param("cardId")long cardId,@Param("storeId")long storeId);

    @Query("select o from OrderEntity o where  o.cardEntity.id=:cardId and o.storeEntity.id=:storeId and o.commonColumn.state=:state")
    List<OrderEntity> selectForRefundAll(@Param("state") int trueState, @Param("cardId") long cardId, @Param("storeId") long storeId);

    @Modifying
    @Query("update OrderEntity o set o.commonColumn.state=:state where o.cardEntity.id=:cardId and o.storeEntity.id=:storeId")
    Integer updateStateByCardId(@Param("state") int state, @Param("cardId") long cardIde, @Param("storeId") long storeId);
}
