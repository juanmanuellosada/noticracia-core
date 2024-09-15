package noticracia.entities;

import java.util.Observable;

@SuppressWarnings("deprecation")
public abstract class InformationSource extends Observable {
    public abstract void startInformationCollection(String query);
}
