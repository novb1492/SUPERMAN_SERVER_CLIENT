package com.kimcompany.jangbogbackendver2.Cart.Service;

import com.kimcompany.jangbogbackendver2.Cart.Repo.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartSelectService {
    private final CartRepo cartRepo;


}
