package noticracia.core;

import noticracia.services.information.manager.InformationManager;
import noticracia.entities.InformationSource;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final InformationManager informationManager;

    public Noticracia(InformationManager informationManager) {
        this.informationManager = informationManager;
    }

    public void setQuery(InformationSource informationSource, String query) {
        informationManager.startInformationCollection(informationSource, query);
    }
}