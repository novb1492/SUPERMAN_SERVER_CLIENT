package com.kimcompany.jangbogbackendver2.Product.Repo;

import com.kimcompany.jangbogbackendver2.Product.Model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<ProductEntity,Long>,ProductRepoCustom {
}
