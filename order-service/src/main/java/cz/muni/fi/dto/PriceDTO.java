package cz.muni.fi.dto;

import cz.muni.fi.enums.Currency;
import java.math.BigDecimal;
import java.util.Date;

public class PriceDTO {
	private Long id;

	private BigDecimal priceValue;

	private Currency currency;

	private Date priceStart;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(BigDecimal priceValue) {
		this.priceValue = priceValue;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Date getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Date priceStart) {
		this.priceStart = priceStart;
	}
}
