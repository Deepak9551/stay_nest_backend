package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
public class UrgencyPriceStrategy implements PricingStrategy{

    private  PricingStrategy pricingStrategy;
    @Override
    public BigDecimal calculate_price(Inventory inventory) {

        BigDecimal price = pricingStrategy.calculate_price(inventory);

        LocalDate today = LocalDate.now();
        if(!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7))){
            return price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}
