package cz.muni.fi.utils;

//import org.dozer.util.DozerClassLoader;
//import java.net.URL;

/**
 * This is custom DozerClassLoader which we need to provide to Mapper because of Dozer's reappearing ClassNotFoundException
 * e.g.: https://stackoverflow.com/questions/23522072/dozer-classnotfoundexception-in-osgi-environment
 */
public class MyDozerClassLoader /*implements DozerClassLoader */{
//    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//    @Override
//    public Class<?> loadClass(String className) {
//        try {
//            return classLoader.loadClass(className);
//        } catch (ClassNotFoundException e) {
//            return null;
//        }
//    }
//
//    @Override
//    public URL loadResource(String uri) {
//        return classLoader.getResource(uri);
//    }
}
