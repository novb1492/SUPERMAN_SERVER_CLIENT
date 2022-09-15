package com.kimcompany.jangbogbackendver2.Cart.Repo;

import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepo extends JpaRepository<CartEntity,Long>,CartRepoCustom {

    @Query("update CartEntity c set c.commonColumn.state=:state where c.id=:id and c.clientEntity.id=:cId")
    @Modifying
    Integer updateStateById(@Param("state")int state,@Param("id")long id,@Param("cId")long cId);

    @Query("update CartEntity c set c.count=:count where c.id=:id and c.clientEntity.id=:cId")
    @Modifying
    Integer updateCountById(@Param("id")long id,@Param("cId")long cId,@Param("count")int count);

}
