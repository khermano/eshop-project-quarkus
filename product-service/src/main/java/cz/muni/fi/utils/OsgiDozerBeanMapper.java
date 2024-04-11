package cz.muni.fi.utils;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;

import java.util.List;

public class OsgiDozerBeanMapper implements Mapper {

    private ClassLoader resourceClassLoader;
    private DozerBeanMapper targetMapper;

    public OsgiDozerBeanMapper(ClassLoader classLoader) {
        this.resourceClassLoader = classLoader;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(resourceClassLoader);
        try {
            targetMapper = new DozerBeanMapper();
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    @Override
    public void map(Object source, Object destination, String mapId) throws MappingException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(resourceClassLoader);
        try {
            targetMapper.map(source, destination, mapId);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass, String mapId) throws MappingException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(resourceClassLoader);
        try {
            return targetMapper.map(source, destinationClass, mapId);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(resourceClassLoader);
        try {
            return targetMapper.map(source, destinationClass);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    @Override
    public void map(Object source, Object destination) throws MappingException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(resourceClassLoader);
        try {
            targetMapper.map(source, destination);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }
}
