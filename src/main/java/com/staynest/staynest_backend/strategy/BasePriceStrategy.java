package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public class BasePriceStrategy implements PricingStrategy{


    @Override
    public BigDecimal calculate_price(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
