package noticracia.entities;

import java.util.Observable;

public abstract class InformationSource extends Observable {

    //Este notifica cuando termina
    public abstract void startInformationCollection (String politician);
}
