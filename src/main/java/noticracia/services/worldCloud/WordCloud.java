package noticracia.services.worldCloud;

import java.util.*;
import java.util.stream.Collectors;

public class WordCloud {

    public static Map<String, Integer> generate(Map<String, String> information) {
        return information.values().stream()
                .flatMap(text -> Arrays.stream(text.toLowerCase().split("\\W+")))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }
}
