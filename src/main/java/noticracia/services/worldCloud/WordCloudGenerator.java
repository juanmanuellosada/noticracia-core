package noticracia.services.worldCloud;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generador de nubes de palabras.
 *
 * Esta clase se encarga de generar una nube de palabras a partir de un mapa de información.
 * La nube de palabras resultante queda ordenada por frecuencia de palabra de mayor a menor.
 *
 * @author Noticracia
 */
public class WordCloudGenerator {

    /**
     * Crea una nube de palabras a partir de un mapa de información.
     *
     * @param information Mapa de información.
     * @return Nube de palabras.
     */
    public static Map<String, Integer> generate(Map<String, String> information) {
        return information.values().stream()
                .flatMap(text -> Arrays.stream(text.toLowerCase().split("[ .,;\\n\"'“”‘’]+")))
                .filter((word) -> word.length() > 3)
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }
}
