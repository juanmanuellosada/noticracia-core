package noticracia.services.worldCloud;

import java.util.*;

/**
 * Generador de {@code nubes de palabras.}
 */
public class WordCloud {

    public Map<String, Integer> generate(Map<String,String> informationMap) {
        Map<String, Integer> wordCloud = new HashMap<>();

        for (Map.Entry<String,String> information : informationMap.entrySet()) {
            String[] words = information.getValue().toLowerCase().split("\\W+");
            for (String word : words) {
                wordCloud.put(word, wordCloud.getOrDefault(word, 0) + 1);
            }
        }

        return wordCloud;
    }
}
