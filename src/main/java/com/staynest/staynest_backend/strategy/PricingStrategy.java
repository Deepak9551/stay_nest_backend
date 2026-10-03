package com.staynest.staynest_backend.strategy;

import com.staynest.staynest_backend.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculate_price(Inventory inventory);
}
