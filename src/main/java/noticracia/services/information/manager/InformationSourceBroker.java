package noticracia.services.information.manager;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.worldCloud.WordCloud;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InformationSourceBroker {
    private final Map<String, InformationSource> informationSources;
    private Map<String, String> lastSentInformation = new HashMap<>();
    private final Noticracia noticracia;

    public InformationSourceBroker(Map<String, InformationSource> informationSources, Noticracia noticracia) {
        this.informationSources = informationSources;
        this.noticracia = noticracia;
    }

    public void startInformationCollection(String informationSourceName, String searchCriteria) {
        this.informationSources.get(informationSourceName).startInformationCollection(searchCriteria);
    }

    public void refreshInformation(Map<String, String> information) {
        if (!information.equals(lastSentInformation)) {
            lastSentInformation = new HashMap<>(information);
            Map<String, Integer> wordCloud = WordCloud.generate(information);
            noticracia.receiveWordCloud(wordCloud);
        }
    }

    public Set<String> getInformationSourcesNames() {
        return informationSources.keySet();
    }
}