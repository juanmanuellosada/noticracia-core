package noticracia.services.worldCloud;

import java.util.*;
import java.util.stream.Collectors;

public class WordCloudGenerator {

    public Map<String, Integer> generate(Set<String> information) {
        return information.stream()
                .flatMap(text -> Arrays.stream(text.toLowerCase().split("[ .,;\\n\"'“”‘’]+")))
                .filter(word -> word.length() > 3)
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }

}
