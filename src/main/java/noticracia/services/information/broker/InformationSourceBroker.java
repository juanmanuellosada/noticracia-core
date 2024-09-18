package noticracia.services.information.broker;

import noticracia.core.Noticracia;
import noticracia.entities.InformationSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InformationSourceBroker {
    private final Map<String, String> lastSentInformation = new HashMap<>();
    private final Noticracia noticracia;
    private InformationSource currentInformationSource;

    public InformationSourceBroker(Noticracia noticracia) {
        this.noticracia = noticracia;
    }

    public boolean startInformationCollection(InformationSource informationSource, String searchCriteria) {
        if (Objects.isNull(currentInformationSource) ||
                !currentInformationSource.getName().equals(informationSource.getName())) {
            if (Objects.nonNull(currentInformationSource)) {
                currentInformationSource.stopProcess();
            }
            currentInformationSource = informationSource;
        }
        return currentInformationSource.startProcess(searchCriteria);
    }

    public boolean refreshInformation(Map<String, String> information) {
        if (!information.equals(lastSentInformation)) {
            lastSentInformation.putAll(information);
            noticracia.generateWordCloud(information);
            return true;
        }
        return false;
    }

}