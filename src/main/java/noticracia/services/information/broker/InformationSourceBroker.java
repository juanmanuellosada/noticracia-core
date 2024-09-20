package noticracia.services.information.broker;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InformationSourceBroker {
    private final Map<String, String> lastSentInformation = new HashMap<>();
    private final Noticracia noticracia;
    private InformationSource currentInformationSource;

    public InformationSourceBroker(Noticracia noticracia) {
        this.noticracia = noticracia;
    }

    public boolean startInformationCollection(InformationSource informationSource, String searchCriteria) {
        if (isDifferentSource(informationSource)) {
            Optional.ofNullable(currentInformationSource).ifPresent(InformationSource::stopProcess);
            currentInformationSource = informationSource;
        }
        return currentInformationSource.startProcess(searchCriteria);
    }

    private boolean isDifferentSource(InformationSource informationSource) {
        return Objects.isNull(currentInformationSource) ||
                !currentInformationSource.getName().equals(informationSource.getName());
    }

    public boolean refreshInformation(Map<String, String> information) {
        if (isNewInformation(information)) {
            lastSentInformation.putAll(information);
            noticracia.generateWordCloud(lastSentInformation);
            return true;
        }
        return false;
    }

    private boolean isNewInformation(Map<String, String> information) {
        return !information.isEmpty() && !information.equals(lastSentInformation);
    }

}