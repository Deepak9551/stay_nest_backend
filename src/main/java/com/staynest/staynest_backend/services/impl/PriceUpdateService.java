package com.staynest.staynest_backend.services.impl;

import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.HotelMinPrice;
import com.staynest.staynest_backend.entity.Inventory;
import com.staynest.staynest_backend.repository.HotelMinPriceRepository;
import com.staynest.staynest_backend.repository.HotelRepository;
import com.staynest.staynest_backend.repository.InventoryRepository;
import com.staynest.staynest_backend.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class PriceUpdateService {

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0 0 * * * *" ) // every 1hr
    public void update_price() {

        int page =  0;
        int batch_size = 100;

        while(true){
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batch_size));
            if(hotelPage.isEmpty()){
                break;
            }
            hotelPage.getContent().forEach(this::update_hotel_price);
            page++;

        }
    }
    public void update_hotel_price(Hotel hotel){

        // update hotel price from this date upto 1 year
        LocalDate today = LocalDate.now();
        LocalDate end_date = today.plusYears(1); // temporal obj

        // get all inventories for this hotel
        List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel, today, end_date);

        // update done in inventory table
        update_inventory_price(inventories);

        // update done in hotel_min_price
        update_hotel_min_price(inventories,hotel,today,end_date);
    }

    private void update_hotel_min_price(List<Inventory> inventories,Hotel hotel ,  LocalDate start_date, LocalDate end_date) {

        // < Date - MinPrice >
        // min price for each date
        Map<LocalDate, BigDecimal> daily_min_price = inventories.stream()
                .collect(Collectors.groupingBy(Inventory::getDate, Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))))
                // terminal operation
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        // prepare hotel price in bulk
        List<HotelMinPrice> hotel_price = new ArrayList<>();
        daily_min_price.forEach((date,price)->{
            HotelMinPrice hotelMinPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date).orElse(new HotelMinPrice(hotel, date));
            hotelMinPrice.setPrice(price);
            hotel_price.add(hotelMinPrice);
        });

        hotelMinPriceRepository.saveAll(hotel_price);
    }

    private void update_inventory_price(List<Inventory> inventories) {

        // update dynamic price of each inventory
        inventories.forEach(
                inventory -> {
                    BigDecimal dynamic_price = pricingService.calculate_dynamic_pricing(inventory);
                    inventory.setPrice(dynamic_price);

                }

        );
        inventoryRepository.saveAll(inventories);

    }


}
