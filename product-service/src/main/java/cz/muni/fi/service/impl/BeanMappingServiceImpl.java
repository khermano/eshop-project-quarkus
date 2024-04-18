package cz.muni.fi.service.impl;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import cz.muni.fi.service.BeanMappingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
//import org.dozer.DozerBeanMapper;
//import org.dozer.Mapper;
//import org.dozer.config.BeanContainer;

@ApplicationScoped
public class BeanMappingServiceImpl implements BeanMappingService {

    @Produces
    @ApplicationScoped
    private Mapper getDozer() {
//        BeanContainer.getInstance().setClassLoader(new MyDozerClassLoader());
        return DozerBeanMapperBuilder.buildDefault();
    }

    @Override
    public <T> T mapTo(Object u, Class<T> mapToClass)
    {
        return getDozer().map(u,mapToClass);
    }
}
