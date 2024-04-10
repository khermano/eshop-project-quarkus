package cz.muni.fi.service;

public interface BeanMappingService {
    <T> T mapTo(Object u, Class<T> mapToClass);
}
