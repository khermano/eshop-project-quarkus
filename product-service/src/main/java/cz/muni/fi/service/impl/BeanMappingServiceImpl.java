package cz.muni.fi.service.impl;

import cz.muni.fi.service.BeanMappingService;
import cz.muni.fi.utils.MyDozerClasLoader;
import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.config.BeanContainer;
import org.dozer.util.DozerClassLoader;

@ApplicationScoped
public class BeanMappingServiceImpl implements BeanMappingService {

    @Produces
    @ApplicationScoped
    private Mapper getDozer() {


        BeanContainer.getInstance().setClassLoader(new MyDozerClasLoader());
        return new DozerBeanMapper();
    }

    @Override
    public <T> T mapTo(Object u, Class<T> mapToClass)
    {
        return getDozer().map(u,mapToClass);
    }
}
