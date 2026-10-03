package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    public BigDecimal calculate_dynamic_pricing(Inventory inventory){

        PricingStrategy pricing_strategy = new BasePriceStrategy();

        // Pricing Strategies
        pricing_strategy = new SurgePricingStrategy(pricing_strategy);
        pricing_strategy = new OccupancyPriceStrategy(pricing_strategy);
        pricing_strategy = new UrgencyPriceStrategy(pricing_strategy);
        pricing_strategy = new HolidayPriceStrategy(pricing_strategy);

        return pricing_strategy.calculate_price(inventory);

    }
}
