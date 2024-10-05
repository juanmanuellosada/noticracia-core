package noticracia.entities;

import java.util.Map;

public class InformationSourceNull implements InformationSource {

    @Override
    public Map<String, String> startSearch(String searchCriteria) {
        return null;
    }

    @Override
    public String getName() {
        return "InformationSourceNull";
    }
}

