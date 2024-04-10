package cz.muni.fi.service.impl;

import cz.muni.fi.service.BeanMappingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

@ApplicationScoped
public class BeanMappingServiceImpl implements BeanMappingService {
    @Produces
    @ApplicationScoped
    private Mapper getDozer() {
        return new DozerBeanMapper();
    }

    @Override
    public <T> T mapTo(Object u, Class<T> mapToClass)
    {
        return getDozer().map(u,mapToClass);
    }
}
