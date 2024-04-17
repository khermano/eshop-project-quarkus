package cz.muni.fi.service;

import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.entity.Price;
import cz.muni.fi.entity.Product;
import cz.muni.fi.enums.Currency;
import cz.muni.fi.exception.EshopServiceException;
import cz.muni.fi.repository.PriceRepository;
import cz.muni.fi.repository.ProductRepository;
import cz.muni.fi.service.impl.ProductServiceImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class ProductServiceTest {
    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BeanMappingService beanMappingService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    
    @BeforeEach
    public void prepareTestProduct(){
        MockitoAnnotations.openMocks(this);

    	testProduct = new Product();
        Price price = new Price();
        price.setCurrency(Currency.EUR);
        price.setPriceValue(BigDecimal.valueOf(10));
        testProduct.setCurrentPrice(price);
    }
    
    @Test
    public void getPriceValueInCurrency(){
        Price price = new Price();
        price.setCurrency(Currency.CZK);
        price.setPriceValue(BigDecimal.valueOf(27));
        Product p = new Product();
        p.setCurrentPrice(price);
        
        BigDecimal priceValueInCurrency = productService.getPriceValueInCurrency(p, Currency.CZK);
        Assertions.assertEquals(0, priceValueInCurrency.compareTo(BigDecimal.valueOf(27)));

        priceValueInCurrency = productService.getPriceValueInCurrency(p, Currency.EUR);
        Assertions.assertEquals(1, priceValueInCurrency.compareTo(BigDecimal.valueOf(1)), priceValueInCurrency.toPlainString());
    }

    @Test
    public void priceChangeByTooMuch(){
        NewPriceDTO newPrice = new NewPriceDTO();
        newPrice.setCurrency(Currency.CZK);
        newPrice.setPriceValue(BigDecimal.valueOf(298));

        Assertions.assertThrows(EshopServiceException.class, () -> productService.changePrice(testProduct, newPrice));
    }

    @Test
    public void acceptablePriceChange(){
        NewPriceDTO newPrice = new NewPriceDTO();
        newPrice.setCurrency(Currency.CZK);
        newPrice.setPriceValue(BigDecimal.valueOf(297));

        Price price = new Price();
        price.setCurrency(Currency.CZK);
        price.setPriceValue(BigDecimal.valueOf(297));

        doReturn(price).when(beanMappingService).mapTo(newPrice, Price.class);

        productService.changePrice(testProduct, newPrice);

        Assertions.assertEquals(testProduct.getCurrentPrice().getCurrency(), newPrice.getCurrency());
        Assertions.assertEquals(testProduct.getCurrentPrice().getPriceValue(), newPrice.getPriceValue());
    }
}
