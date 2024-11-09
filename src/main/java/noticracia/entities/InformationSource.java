package noticracia.entities;

import java.util.Set;

public interface InformationSource {

    public Set<String> getInformation(String politicalCandidate);

    public String getName();
}
