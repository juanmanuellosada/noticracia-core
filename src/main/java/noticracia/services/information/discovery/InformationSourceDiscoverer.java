package noticracia.services.information.discovery;

import noticracia.entities.InformationSource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Clase encargada de descubrir las fuentes de información en JARs en un directorio.
 *
 * @author Noticracia
 */
public class InformationSourceDiscoverer {
    private final Set<Class<? extends InformationSource>> classes = new HashSet<>();

    /**
     * Busca los JARs en el directorio especificado y los procesa para extraer las fuentes de información.
     *
     * @param directoryPath la ruta del directorio que se va a buscar.
     * @return el conjunto de fuentes de información encontradas.
     */
    public Set<Class<? extends InformationSource>> discover(String directoryPath) {
        File[] files = loadJarFiles(directoryPath);

        for (File file : files) {
            processJarFile(file);
        }
        return new HashSet<>(this.classes);
    }

    /**
     * Carga los JARs en el directorio especificado.
     *
     * @param directoryPath la ruta del directorio que se va a buscar.
     * @return el array de JARs encontrados.
     */
    private File[] loadJarFiles(String directoryPath) {
        File dir = new File(directoryPath);
        return dir.listFiles((d, name) -> name.endsWith(".jar"));
    }

    /**
     * Procesa el JAR especificado y extrae las fuentes de información.
     *
     * @param file el JAR que se va a procesar.
     */
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

    /**
     * Procesa las entradas del JAR y extrae las fuentes de información.
     *
     * @param jarFile el JAR que se va a procesar.
     * @param cl      el ClassLoader que se va a usar.
     */
    private void processEntries(JarFile jarFile, URLClassLoader cl) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
                loadClass(className, cl);
            }
        }
    }

    /**
     * Carga la clase especificada y la agrega al conjunto de fuentes de información si es una
     * fuente de información.
     *
     * @param className el nombre de la clase que se va a cargar.
     * @param cl        el ClassLoader que se va a usar.
     */
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

