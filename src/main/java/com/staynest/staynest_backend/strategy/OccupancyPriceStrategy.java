package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
public class OccupancyPriceStrategy implements PricingStrategy {

    private  PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculate_price(Inventory inventory) {
        BigDecimal price = pricingStrategy.calculate_price(inventory);
        // adding occupany strategy
        // how much portion booked room cover from total room
        // ex -  A / B = 0.7 ( means A cover 70 % part of B )

        // how many were room occupied
        double occupancy_rate = (double) inventory.getBookedCount() / inventory.getTotalCount();

        if(occupancy_rate>=0.7){
            return price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
