package com.kimcompany.jangbogbackendver2.Deliver.Repo;

import com.kimcompany.jangbogbackendver2.Deliver.Model.DeliverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliverRepo extends JpaRepository<DeliverEntity,Long>,DeliverRepoCustom {

    @Query("select  d from DeliverEntity d where d.id=:id and d.storeEntity.id=:storeId")
    Optional<DeliverEntity> findByStoreIdAndStateAndId( @Param("id") long deliverId, @Param("storeId") long storeId);

    @Modifying
    @Query("update DeliverEntity d set d.commonColumn.state=:state where d.id=:id and d.storeEntity.id=:storeId")
    Integer updateState(@Param("id") long deliverId, @Param("state") int state,@Param("storeId") long storeId);

}
