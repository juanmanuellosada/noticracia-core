package noticracia.services.information.discovery;

import noticracia.entities.InformationSource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InformationSourceDiscoverer {

    private final Set<Class<? extends InformationSource>> classes = new HashSet<>();


    public Set<Class<? extends InformationSource>> discover(String directoryPath) {
        File[] files = loadJarFiles(directoryPath);

        for (File file : files) {
            processJarFile(file);
        }
        return new HashSet<>(this.classes);
    }

    private File[] loadJarFiles(String directoryPath) {
        return Optional.ofNullable(new File(directoryPath).listFiles())
                .map(files -> Arrays.stream(files)
                        .filter(file -> file.getName().endsWith(".jar"))
                        .toArray(File[]::new))
                .orElse(new File[0]);
    }

    @SuppressWarnings("deprecation")
    private void processJarFile(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
            try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
                processEntries(jarFile, cl);
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL Exception for file: " + file.getAbsolutePath() + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception reading JAR file: " + file.getAbsolutePath() + " - " + e.getMessage());
        }
    }

    private void processEntries(JarFile jarFile, URLClassLoader cl) {
        jarFile.stream()
                .filter(entry -> entry.getName().endsWith(".class") && !entry.isDirectory())
                .map(entry -> entry.getName().replace('/', '.').replace(".class", ""))
                .forEach(className -> loadClass(className, cl));
    }

    @SuppressWarnings("unchecked")
    private void loadClass(String className, URLClassLoader cl) {
        try {
            Class<?> cls = cl.loadClass(className);
            if (InformationSource.class.isAssignableFrom(cls) && !cls.isInterface()) {
                this.classes.add((Class<? extends InformationSource>) cls);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load class: " + className);
        } catch (Throwable e) {
            System.err.println("Failed to load class: " + className + " - " + e.getMessage());
        }
    }
}

