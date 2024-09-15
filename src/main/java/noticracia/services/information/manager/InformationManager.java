package noticracia.services.information.manager;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;
import noticracia.services.worldCloud.WordCloud;

import java.util.*;

@SuppressWarnings("deprecation")
public class InformationManager implements Observer {
    private Set<InformationSource> informationSources;
    private Map<String, String> lastSentInformation = new HashMap<>();
    private final Noticracia noticracia;

    public InformationManager(Set<InformationSource> informationSources, Noticracia noticracia) {
        this.informationSources = informationSources;
        this.noticracia = noticracia;

        for (InformationSource informationSource : informationSources) {
            informationSource.addObserver(this);
        }
    }
    public void startInformationCollection(InformationSource informationSource, String query) {
        informationSource.startInformationCollection(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if (o instanceof InformationSource && arg instanceof Map) {
            Map<String, String> currentInformation = (Map<String, String>) arg;
            if (hasNewInformation(currentInformation)) {
                lastSentInformation = new HashMap<>(currentInformation);
                Map<String, Integer> wordCloud = WordCloud.generate(currentInformation);
                noticracia.receiveWordCloud(wordCloud);
            }
        }
    }

    private boolean hasNewInformation(Map<String, String> currentInformation) {
        return !currentInformation.equals(lastSentInformation);
    }
}