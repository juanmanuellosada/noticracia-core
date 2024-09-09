package noticracia.services.worldCloud;

import noticracia.entities.Information;
import noticracia.entities.InformationSource;

import java.util.*;

/**
 * Generador de {@code nubes de palabras.}
 */
public class WordCloudGenerator {

    /**
     * Genera una {@code nube de palabras} a partir del {@code nombre de un politico} y una {@link noticracia.entities.InformationSource fuente de información}.
     *
     * @param politicianName {@link String}
     * @param source {@link noticracia.entities.InformationSource InformationSource}
     * @return nube de palabras {@link Map}<{@link String}, {@link Integer}>
     */
    public Map<String, Integer> generate(String politicianName, InformationSource source) {
        Map<String, Integer> wordCloud = new HashMap<>();

        Set<Information> informationSet = source.getInformation(politicianName);

        informationSet.forEach(information -> {
            // Convertir a minúsculas y dividir el string en palabras
            String[] words = information.getText().toLowerCase().split("\\W+");

            // Contar las apariciones de cada palabra
            for (String word : words) {
                wordCloud.put(word, wordCloud.getOrDefault(word, 0) + 1);
            }
        });

        return wordCloud;
    }

}
