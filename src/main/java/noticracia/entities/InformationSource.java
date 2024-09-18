package noticracia.entities;

import noticracia.services.information.broker.InformationSourceBroker;

import java.util.Map;

public abstract class InformationSource {
    private final InformationSourceBroker informationSourceBroker;

    public InformationSource(InformationSourceBroker informationSourceBroker) {
        this.informationSourceBroker = informationSourceBroker;
    }

    public void startInformationCollection(String searchCriteria){
        refresh(process(searchCriteria));
    }

    public abstract Map<String, String> process(String searchCriteria);

    public abstract String getName();

    private void refresh(Map<String, String> information) {
        informationSourceBroker.refreshInformation(information);
    }
}
