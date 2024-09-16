package noticracia.entities;


import noticracia.services.information.manager.InformationManager;

import java.util.Map;

public abstract class InformationSource {

    private final InformationManager informationManager;

    public InformationSource(InformationManager informationManager) {
        this.informationManager = informationManager;
    }
    public void startInformationCollection(String query){
        postProcess(processQuery(query));
    }
    public abstract Map<String, String> processQuery(String query);

    public abstract String getName();

    private void postProcess(Map<String, String> information) {
        informationManager.refreshInformation(information);
    }
}
