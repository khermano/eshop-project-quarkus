package cz.muni.fi.repository;

import cz.muni.fi.entity.Price;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PriceRepository implements PanacheRepositoryBase<Price, Long> {
}
