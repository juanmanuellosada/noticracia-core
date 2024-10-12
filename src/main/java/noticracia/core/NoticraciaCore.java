package noticracia.core;

import noticracia.entities.InformationSource;

import java.util.*;

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
        validateInput(informationSourceName, searchCriteria);

        InformationSource informationSource = getInformationSource(informationSourceName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown information source: " + informationSourceName));

        switchInformationSourceIfNeeded(informationSource);

        currentInformationSource.start(searchCriteria);
    }

    private void validateInput(String informationSourceName, String searchCriteria) {
        if (informationSourceName == null || informationSourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Information source name cannot be null or empty.");
        }
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            throw new IllegalArgumentException("Search criteria cannot be null or empty.");
        }
    }

    private Optional<InformationSource> getInformationSource(String informationSourceName) {
        return Optional.ofNullable(this.informationSources.get(informationSourceName));
    }

    private void switchInformationSourceIfNeeded(InformationSource newSource) {
        if (isDifferentSource(newSource)) {
            stopCurrentInformationSource();
            currentInformationSource = newSource;
        }
    }

    private void stopCurrentInformationSource() {
        if (currentInformationSource != null) {
            currentInformationSource.stop();
            currentInformationSource = null;
        }
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

    public void addInformationSources(Map<String, InformationSource> newInformationSources) {
        newInformationSources.forEach((name, informationSource) -> {
            if (!informationSources.containsKey(name)) {
                informationSources.put(name, informationSource);
            }
        });
    }

}