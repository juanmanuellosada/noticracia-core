package noticracia.entities;

import noticracia.services.information.manager.InformationSourceBroker;

import java.util.Map;

public abstract class InformationSource {

    private final InformationSourceBroker informationSourceBroker;

    public InformationSource(InformationSourceBroker informationSourceBroker) {
        this.informationSourceBroker = informationSourceBroker;
    }

    public void startInformationCollection(String query){
        postProcess(process(query));
    }

    public abstract Map<String, String> process(String query);

    public abstract String getName();

    private void postProcess(Map<String, String> information) {
        informationSourceBroker.refreshInformation(information);
    }
}
