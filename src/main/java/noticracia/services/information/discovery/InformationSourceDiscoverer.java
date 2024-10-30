package noticracia.services.information.discovery;

import noticracia.entities.InformationSource;
import noticracia.entities.InformationSourceNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InformationSourceDiscoverer {

    public Class<? extends InformationSource> discover(String directoryPath) {
        File[] jarFiles = loadJarFiles(directoryPath);
        for (File jarFile : jarFiles) {
            Optional<Class<? extends InformationSource>> discoveredClass = processJarFile(jarFile);
            if (discoveredClass.isPresent()) {
                return discoveredClass.get();
            }
        }
        return InformationSourceNull.class;
    }

    private File[] loadJarFiles(String directoryPath) {
        File dir = new File(directoryPath);
        File[] jarFiles = dir.listFiles((d, name) -> name.endsWith(".jar"));
        return jarFiles != null ? jarFiles : new File[0];
    }

    @SuppressWarnings("deprecation")
    private Optional<Class<? extends InformationSource>> processJarFile(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            URL[] urls = {new URL("jar:file:" + jarFile.getAbsolutePath() + "!/")};
            try (URLClassLoader classLoader = URLClassLoader.newInstance(urls)) {
                return processEntries(jar, classLoader);
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL Exception for file: {}" + jarFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("IO Exception reading JAR file: {}" + jarFile.getAbsolutePath());
        }
        return Optional.empty();
    }

    private Optional<Class<? extends InformationSource>> processEntries(JarFile jarFile, URLClassLoader classLoader) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
                Optional<Class<? extends InformationSource>> cls = loadClass(className, classLoader);
                if (cls.isPresent()) {
                    return cls;
                }
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<? extends InformationSource>> loadClass(String className, URLClassLoader classLoader) {
        try {
            Class<?> cls = classLoader.loadClass(className);
            if (InformationSource.class.isAssignableFrom(cls) && !cls.isInterface()) {
                return Optional.of((Class<? extends InformationSource>) cls);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load class: {}" + className);
        }
        return Optional.empty();
    }
}

