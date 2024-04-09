package cz.muni.fi.service;

import cz.muni.fi.dto.NewPriceDTO;
import cz.muni.fi.entity.Product;
import cz.muni.fi.enums.Currency;
import java.math.BigDecimal;

public interface ProductService {
	Product createProduct(Product p);
	void addCategory(Long productId, Long categoryId);
	void changePrice(Product product, NewPriceDTO newPrice);
	BigDecimal getPriceValueInCurrency(Product p, Currency currency);
	BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo);
}
