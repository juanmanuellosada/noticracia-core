package noticracia.core;

import noticracia.entities.InformationSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NoticraciaCore {

    private final Noticracia noticracia;
    protected final Map<String, InformationSource> informationSources;
    private InformationSource currentInformationSource;
    private final Map<String, String> lastSentInformation = new HashMap<>();

    public NoticraciaCore(Noticracia noticracia, Map<String, InformationSource> informationSources) {
        this.noticracia = noticracia;
        this.informationSources = informationSources;
        this.informationSources.forEach((name, informationSource) -> informationSource.setCore(this));
    }

    protected void startSearch(String informationSourceName, String searchCriteria) {
        InformationSource informationSource = this.informationSources.get(informationSourceName);
        if (isDifferentSource(informationSource)) {
            Optional.ofNullable(currentInformationSource).ifPresent(InformationSource::stop);
            currentInformationSource = informationSource;
        }
        currentInformationSource.start(searchCriteria);
    }

    public void refreshInformation(Map<String, String> information) {
        if (isNewInformation(information)) {
            lastSentInformation.putAll(information);
            noticracia.generateWordCloud(lastSentInformation);
        }
    }

    private boolean isDifferentSource(InformationSource informationSource) {
        return Objects.isNull(currentInformationSource) ||
                !currentInformationSource.getName().equals(informationSource.getName());
    }

    private boolean isNewInformation(Map<String, String> information) {
        return !information.isEmpty() && !isContained(information, lastSentInformation);
    }

    private boolean isContained(Map<String, String> information, Map<String, String> lastSentInformation) {
        return information.entrySet().stream()
                .allMatch(e -> e.getValue().equals(lastSentInformation.get(e.getKey())));
    }

}