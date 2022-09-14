package com.kimcompany.jangbogbackendver2.Cart.Repo;

import com.kimcompany.jangbogbackendver2.Cart.Model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<CartEntity,Long> {
}
