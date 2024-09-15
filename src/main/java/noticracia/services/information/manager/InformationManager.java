package noticracia.services.information.manager;

import noticracia.entities.InformationSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class InformationManager extends Observable implements Observer {
    private Map<String, String> lastSentInformation = new HashMap<>();

    public void startInformationCollection(InformationSource informationSource, String query) {
        informationSource.addObserver(this);
        informationSource.startInformationCollection(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if (o instanceof InformationSource && arg instanceof Map) {
            Map<String, String> currentInformation = (Map<String, String>) arg;
            notifyObservers(currentInformation);
        }
    }

    private void notifyObservers(Map<String, String> currentInformation) {
        if (hasNewInformation(currentInformation)) {
            lastSentInformation = new HashMap<>(currentInformation);
            setChanged();
            notifyObservers(currentInformation);
        }
    }

    private boolean hasNewInformation(Map<String, String> currentInformation) {
        return !currentInformation.equals(lastSentInformation);
    }
}