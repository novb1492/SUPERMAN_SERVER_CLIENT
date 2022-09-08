package com.kimcompany.jangbogbackendver2.ProductEvent.Repo;

import com.kimcompany.jangbogbackendver2.ProductEvent.Model.ProductEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEventRepo extends JpaRepository<ProductEventEntity,Long> {
}
