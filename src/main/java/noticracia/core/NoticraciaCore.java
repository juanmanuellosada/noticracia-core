package noticracia.core;

import noticracia.entities.InformationSource;

import java.util.*;

public class NoticraciaCore {

    private final Map<String, InformationSource> informationSources;

    public NoticraciaCore(Map<String, InformationSource> initialSources) {
        this.informationSources = new HashMap<>(initialSources);
    }

    public Set<String> getInformation(String politicalCandidate, String informationSourceName) {
        return Optional.ofNullable(informationSources.get(informationSourceName))
                .orElseThrow(() -> new IllegalArgumentException("Source not found: " + informationSourceName))
                .getInformation(politicalCandidate);
    }

    public void addNewInformationSources(Map<String, InformationSource> newInformationSources) {
        newInformationSources.forEach((name, source) -> {
            if (!informationSources.containsKey(name)) {
                informationSources.put(name, source);
            }
        });
    }

    public boolean hasSource(String sourceName) {
        return informationSources.containsKey(sourceName);
    }

    public Map<String, InformationSource> getInformationSources() {
        return Collections.unmodifiableMap(informationSources);
    }
}