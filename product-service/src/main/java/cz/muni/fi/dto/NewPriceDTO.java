package cz.muni.fi.dto;

import cz.muni.fi.enums.Currency;
import java.math.BigDecimal;

public class NewPriceDTO {
    private BigDecimal priceValue;

    private Currency currency;

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
}
