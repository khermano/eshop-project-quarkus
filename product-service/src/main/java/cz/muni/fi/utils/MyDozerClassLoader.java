package cz.muni.fi.utils;

import org.dozer.util.DozerClassLoader;
import java.net.URL;

public class MyDozerClassLoader implements DozerClassLoader {

    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    @Override
    public Class<?> loadClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public URL loadResource(String uri) {
        return classLoader.getResource(uri);
    }
}
