package noticracia.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WordCloud {

    private final Map<String, Integer> wordCloud;

    public WordCloud(Set<String> information) {
        wordCloud = information.stream()
                .flatMap(text -> Arrays.stream(text.toLowerCase().split("[ .,;\\n\"'“”‘’]+")))
                .filter(word -> word.length() > 3)
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }

    public Map<String, Integer> getWordCloud() {
        return Collections.unmodifiableMap(wordCloud);
    }

}
