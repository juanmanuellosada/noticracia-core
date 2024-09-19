package noticracia.entities;

import noticracia.services.information.broker.InformationSourceBroker;

import java.util.Map;

public abstract class InformationSource {
    private final InformationSourceBroker informationSourceBroker;

    public InformationSource(InformationSourceBroker informationSourceBroker) {
        this.informationSourceBroker = informationSourceBroker;
    }

    public abstract boolean startProcess(String searchCriteria);

    public abstract void stopProcess();

    public abstract String getName();

    public final boolean refresh(Map<String, String> information) {
        return informationSourceBroker.refreshInformation(information);
    }
}
