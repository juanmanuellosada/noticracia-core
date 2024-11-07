package noticracia.core;

import noticracia.entities.InformationSource;

import java.util.Map;
import java.util.Objects;

public class NoticraciaCore {

    public Map<String, InformationSource> informationSources;

    public NoticraciaCore(Map<String, InformationSource> informationSources) {
        this.informationSources = Objects.requireNonNull(informationSources, "Information sources cannot be null");
        if (informationSources.isEmpty()) {
            throw new IllegalArgumentException("Information sources cannot be empty");
        }
    }
}
