package com.kimcompany.jangbogbackendver2.ProductKind.Repo;

import com.kimcompany.jangbogbackendver2.ProductKind.Dto.SelectDto;
import com.kimcompany.jangbogbackendver2.ProductKind.Model.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryEntityRepo extends JpaRepository<ProductCategoryEntity,Long> {

    @Query("select new com.kimcompany.jangbogbackendver2.ProductKind.Dto.SelectDto(p.name,p.id) from ProductCategoryEntity p where p.commonColumn.state<>:state")
    List<SelectDto>findAllToDto(@Param("state")int state);

    @Query("select p from ProductCategoryEntity p where p.commonColumn.state<>:state and p.id=:id")
    Optional<ProductCategoryEntity>findById(@Param("state")int state,@Param("id")long id);
}
