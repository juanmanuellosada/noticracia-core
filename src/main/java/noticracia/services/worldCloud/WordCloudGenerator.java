package noticracia.services.worldCloud;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class WordCloudGenerator {
    public static Map<String, Integer> generate(Map<String, String> information) {
        return information.values().stream()
                .flatMap(text -> Arrays.stream(text.toLowerCase().split("[ .,;\\n\"'“”‘’]+")))
                .filter((word) -> word.length() > 3)
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }
}
