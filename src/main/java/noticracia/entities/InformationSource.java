package noticracia.entities;

import java.util.Map;

public interface InformationSource {

    public Map<String, String> getInformation(String searchCriteria);

    public String getName();
}
