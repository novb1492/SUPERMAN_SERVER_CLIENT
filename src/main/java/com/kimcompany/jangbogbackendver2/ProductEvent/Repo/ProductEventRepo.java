package com.kimcompany.jangbogbackendver2.ProductEvent.Repo;

import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductEventRepo extends JpaRepository<ProductEventEntity,Long> {

    @Query("select p from ProductEventEntity p where  p.productEntity.id=:id and p.startDate<=:now and p.endDate>=:now and p.commonColumn.state=:state")
    Optional<ProductEventEntity> findProductId(@Param("id")long productId, @Param("now") Timestamp now,@Param("state")int trueState);

    @Query("select p from ProductEventEntity p where  p.productEntity.id=:productId and p.startDate<=:now and p.endDate>=:now and p.id=:id and p.commonColumn.state=:state")
    Optional<ProductEventEntity> findProductIdAndId(@Param("productId")long productId, @Param("now") Timestamp now,@Param("id")long id,@Param("state")int trueState);

}
