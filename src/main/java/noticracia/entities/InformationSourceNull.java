package noticracia.entities;

public class InformationSourceNull implements InformationSource {

    @Override
    public boolean startSearch(String searchCriteria) {
        return false;
    }

    @Override
    public String getName() {
        return "InformationSourceNull";
    }
}

