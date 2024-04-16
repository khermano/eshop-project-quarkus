package cz.muni.fi.service.impl;

import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.entity.Price;
import cz.muni.fi.entity.Product;
import cz.muni.fi.enums.Currency;
import cz.muni.fi.exception.EshopServiceException;
import cz.muni.fi.repository.PriceRepository;
import cz.muni.fi.repository.ProductRepository;
import cz.muni.fi.service.BeanMappingService;
import cz.muni.fi.service.ProductService;
import cz.muni.fi.stork.CategoryClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {
	@Inject
	private ProductRepository productRepository;

	@Inject
	private PriceRepository priceRepository;

	@RestClient
	private CategoryClient categoryClient;

	@Inject
	private BeanMappingService beanMappingService;

	private static final Map<AbstractMap.SimpleEntry<Currency, Currency>, BigDecimal> currencyRateCache = new HashMap<>();

	static {
		AbstractMap.SimpleEntry<Currency, Currency> czk_usd = new AbstractMap.SimpleEntry<>(Currency.CZK,
				Currency.USD);
		AbstractMap.SimpleEntry<Currency, Currency> czk_eur = new AbstractMap.SimpleEntry<>(Currency.CZK,
				Currency.EUR);
		AbstractMap.SimpleEntry<Currency, Currency> czk_czk = new AbstractMap.SimpleEntry<>(Currency.CZK,
				Currency.CZK);

		AbstractMap.SimpleEntry<Currency, Currency> usd_czk = new AbstractMap.SimpleEntry<>(Currency.USD,
				Currency.CZK);
		AbstractMap.SimpleEntry<Currency, Currency> usd_eur = new AbstractMap.SimpleEntry<>(Currency.USD,
				Currency.EUR);
		AbstractMap.SimpleEntry<Currency, Currency> usd_usd = new AbstractMap.SimpleEntry<>(Currency.USD,
				Currency.USD);

		AbstractMap.SimpleEntry<Currency, Currency> eur_usd = new AbstractMap.SimpleEntry<>(Currency.EUR,
				Currency.USD);
		AbstractMap.SimpleEntry<Currency, Currency> eur_czk = new AbstractMap.SimpleEntry<>(Currency.EUR,
				Currency.CZK);
		AbstractMap.SimpleEntry<Currency, Currency> eur_eur = new AbstractMap.SimpleEntry<>(Currency.EUR,
				Currency.EUR);

		currencyRateCache.put(czk_czk, BigDecimal.valueOf(1));
		currencyRateCache.put(czk_eur, BigDecimal.valueOf(0.04));
		currencyRateCache.put(czk_usd, BigDecimal.valueOf(0.04));

		currencyRateCache.put(usd_czk, BigDecimal.valueOf(27));
		currencyRateCache.put(usd_eur, BigDecimal.valueOf(1));
		currencyRateCache.put(usd_usd, BigDecimal.valueOf(1));

		currencyRateCache.put(eur_czk, BigDecimal.valueOf(27));
		currencyRateCache.put(eur_eur, BigDecimal.valueOf(1));
		currencyRateCache.put(eur_usd, BigDecimal.valueOf(1));
	}

	@Override
	public Product createProduct(Product product) {
        List<Price> priceHistory = product.getPriceHistory();
        for (Price price : priceHistory) {
			priceRepository.persist(price);
		}
		productRepository.persist(product);
		return product;
	}

	@Override
	public BigDecimal getPriceValueInCurrency(Product p, Currency currency) {
		BigDecimal convertRate = getCurrencyRate(
				p.getCurrentPrice().getCurrency(), currency);

        return p.getCurrentPrice().getValue().multiply(convertRate)
				.setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public void changePrice(Product p, NewPriceDTO newPrice) {
		BigDecimal oldPriceInNewCurrency = getPriceValueInCurrency(p, newPrice.getCurrency());

		BigDecimal difference = oldPriceInNewCurrency
				.subtract(newPrice.getValue());
		BigDecimal percents = difference.abs().divide(
				oldPriceInNewCurrency, 5, RoundingMode.HALF_UP);
		if (percents.compareTo(BigDecimal.valueOf(0.1)) > 0) {
			throw new EshopServiceException(
					"It is not allowed to change the price by more than 10%");
		}
		Price price = beanMappingService.mapTo(newPrice, Price.class);
		price.setPriceStart(new Date());
		priceRepository.persist(price);
		p.addHistoricalPrice(p.getCurrentPrice());
		p.setCurrentPrice(price);
		productRepository.persist(p);
	}

	@Override
	public void addCategory(Long productId, Long categoryId) {
		Product product = productRepository.findById(productId);

		if(product == null) {
			throw new EshopServiceException("Product with given ID doesn't exist");
		}
		if (product.getCategoriesId().contains(categoryId)) {
			throw new EshopServiceException(
					"Product already contains this category. Product: "
							+ productId + ", category: "
							+ categoryId);
		}
		if (categoryClient.getCategory(categoryId).getStatus() == HttpStatus.SC_OK) {
			product.addCategoryId(categoryId);
			productRepository.persist(product);
		} else {
			throw new EshopServiceException("There is a problem retrieving category with given ID");
		}
	}

	@Override
	public BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo) {
		AbstractMap.SimpleEntry<Currency, Currency> convertCouple = new AbstractMap.SimpleEntry<>(currencyFrom,
				currencyTo);
		if (!currencyRateCache.containsKey(convertCouple)) {
			throw new IllegalArgumentException(
					"There is no existing exchange rate for conversion: "
							+ currencyFrom + "->" + currencyTo);
		}
		return currencyRateCache.get(convertCouple);
	}
}
