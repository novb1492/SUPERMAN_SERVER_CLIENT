package com.kimcompany.jangbogbackendver2.Product.Repo;

import com.kimcompany.jangbogbackendver2.Product.Dto.SelectDto;
import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<ProductEntity,Long>,ProductRepoCustom {
    //---------------------------
    @Query("select new com.kimcompany.jangbogbackendver2.Product.Dto.SelectDto(p)  from ProductEntity p where p.id=:id")
    Optional<SelectDto> findId(@Param("id") long id);

}
