package noticracia.services.information.manager;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.worldCloud.WordCloud;

import java.util.*;
import java.util.stream.Collectors;

public class InformationManager {
    private final Set<InformationSource> informationSources;
    private Map<String, String> lastSentInformation = new HashMap<>();
    private final Noticracia noticracia;

    public InformationManager(Set<InformationSource> informationSources, Noticracia noticracia) {
        this.informationSources = informationSources;
        this.noticracia = noticracia;
    }
    public void startInformationCollection(InformationSource informationSource, String query) {
        informationSource.startInformationCollection(query);
    }

    public void refreshInformation(Map<String, String> information) {
        if (!information.equals(lastSentInformation)) {
            lastSentInformation = new HashMap<>(information);
            Map<String, Integer> wordCloud = WordCloud.generate(information);
            noticracia.receiveWordCloud(wordCloud);
        }
    }

    public Set<String> getInformationSourcesNames() {
        return informationSources.stream().map(InformationSource::getName).collect(Collectors.toSet());
    }
}