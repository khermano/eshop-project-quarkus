package cz.muni.fi.repository;

import cz.muni.fi.entity.Price;
import cz.muni.fi.enums.Currency;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;

@QuarkusTest
@Transactional
public class PriceRepositoryTest {
    @Inject
    public PriceRepository priceRepository;

    private Price savedPrice;

    @BeforeEach
    public void createPrice(){
        savedPrice = new Price();
        savedPrice.setCurrency(Currency.CZK);
        savedPrice.setPriceStart(new Date());
        savedPrice.setValue(BigDecimal.valueOf(1001));

        priceRepository.persist(savedPrice);
    }

    @AfterEach
    public void deletePrice() {
        priceRepository.delete(savedPrice);
    }

    @Test
    public void create(){
        Price foundPrice = priceRepository.findById(savedPrice.getId());
        Assertions.assertEquals(savedPrice.getPriceStart(), foundPrice.getPriceStart());
    }

    @Test
    public void update(){
        priceRepository.update("value = ?1 where id = ?2", BigDecimal.valueOf(2), savedPrice.getId());
        Price found = priceRepository.findById(savedPrice.getId());
        Assertions.assertEquals(new BigDecimal("2.00"), found.getValue());
    }
}
