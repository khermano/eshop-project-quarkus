package cz.muni.fi.utils;

//import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import org.dozer.util.DozerClassLoader;
import java.net.URL;

public class MyDozerClasLoader implements DozerClassLoader {

    private ClassLoader cl = Thread.currentThread().getContextClassLoader();
    @Override
    public Class<?> loadClass(String className) {
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public URL loadResource(String uri) {

        return cl.getResource(uri);
    }
}
