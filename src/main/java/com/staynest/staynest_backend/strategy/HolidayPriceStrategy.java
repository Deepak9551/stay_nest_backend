package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@AllArgsConstructor
public class HolidayPriceStrategy implements PricingStrategy{

    private  PricingStrategy pricingStrategy;


    @Override
    public BigDecimal calculate_price(Inventory inventory) {
       BigDecimal price  = pricingStrategy.calculate_price(inventory);

       boolean is_holiday = true;  // TODO: fetch this from API to check

        if(is_holiday){
        return    price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
