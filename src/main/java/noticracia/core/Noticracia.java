package noticracia.core;

import noticracia.configuration.ConfigLoader;
import noticracia.entities.InformationSource;
import noticracia.entities.WordCloud;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final Map<String, InformationSource> informationSources;

    public Noticracia(Map<String, InformationSource> informationSources) {
        this.informationSources = informationSources;
    }

    public WordCloud generateWordCloud(String politicalCandidate) {
        WordCloud wordCloud = new WordCloud(collectInformation(politicalCandidate));
        setChanged();
        notifyObservers(wordCloud);
        return wordCloud;
    }

    private Set<String> collectInformation(String politicalCandidate) {
        return informationSources.values().stream()
                .flatMap(source -> source.getInformation(politicalCandidate).stream())
                .collect(Collectors.toSet());
    }

    public Map<String, InformationSource> getInformationSources() {
        return Collections.unmodifiableMap(informationSources);
    }

    public String[] getPoliticalCandidatesNames() {
        String candidates = new ConfigLoader().getProperty("political.candidates.names");
        return candidates.isEmpty() ? new String[0] : candidates.split(",");
    }
}
