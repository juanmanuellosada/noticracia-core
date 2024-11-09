package noticracia.core;

import noticracia.configuration.ConfigLoader;
import noticracia.entities.InformationSource;
import noticracia.entities.WordCloud;

import java.util.*;
import java.util.stream.Collectors;

public class Noticracia {

    public Map<String, InformationSource> informationSources;

    public Noticracia(Map<String, InformationSource> informationSources) {
        this.informationSources = informationSources;
    }

    public WordCloud generateWordCloud(String politicalCandidate) {

        Set<String> information = this.informationSources.values()
                .stream()
                .flatMap(source -> source.getInformation(politicalCandidate).stream())
                .collect(Collectors.toSet());

        return new WordCloud(information);
    }

    public String[] getPoliticalCandidates() {
        return new ConfigLoader().getPoliticalCandidatesNames();
    }
}
