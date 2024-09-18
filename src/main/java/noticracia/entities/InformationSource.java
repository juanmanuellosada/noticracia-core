package noticracia.entities;

import noticracia.services.information.manager.InformationSourceBroker;

import java.util.Map;

public abstract class InformationSource {
    private final InformationSourceBroker informationSourceBroker;

    public InformationSource(InformationSourceBroker informationSourceBroker) {
        this.informationSourceBroker = informationSourceBroker;
    }

    public void startInformationCollection(String searchCriteria){
        refresh(process(searchCriteria));
    }

    public abstract Map<String, String> process(String query);

    public abstract String getName();

    private void refresh(Map<String, String> information) {
        informationSourceBroker.refreshInformation(information);
    }
}
