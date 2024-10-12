package noticracia.entities;

import noticracia.core.NoticraciaCore;

import java.util.Map;

public abstract class InformationSource {
    private NoticraciaCore core;

    public final void setCore(NoticraciaCore core) {
        this.core = core;
    }

    public abstract void start(String searchCriteria);

    public abstract void stop();

    public abstract String getName();

    public final void notify(Map<String, String> information) {
        this.core.refreshInformation(information);
    }

    public abstract Map<String, String> mapInformation(Object result);
}
