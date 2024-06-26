package cz.muni.fi.entity;

import cz.muni.fi.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Price {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@DecimalMin("0.0")
	@NotNull
	@Column(nullable=false)
	private BigDecimal priceValue;
	
	@NotNull
	private Date priceStart;
	
	@Enumerated
	@NotNull
	private Currency currency;

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

	public Date getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Date priceStart) {
		this.priceStart = priceStart;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((priceStart == null) ? 0 : priceStart.hashCode());
		return prime * result + ((priceValue == null) ? 0 : priceValue.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
        }
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Price other = (Price) obj;
		if (currency != other.currency) {
			return false;
		}
		if (priceStart == null) {
			if (other.priceStart != null) {
				return false;
			}
		}
		else if (!priceStart.equals(other.priceStart)) {
			return false;
		}
		if (priceValue == null) {
            return other.priceValue == null;
		}
		else {
			return priceValue.equals(other.priceValue);
		}
    }
}
