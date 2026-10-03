package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@AllArgsConstructor
public class SurgePricingStrategy implements PricingStrategy{

    private  PricingStrategy pricingStrategy;


    @Override
    public BigDecimal calculate_price(Inventory inventory) {
        return pricingStrategy.calculate_price(inventory).multiply(inventory.getSurgeFactor());
    }
}
