package noticracia.core;

import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    private final NoticraciaCore noticraciaCore;

    public Noticracia(String path) {
        noticraciaCore = new NoticraciaCore(path);
    }

    public void search(String searchCriteria) {
        generateWordCloud(noticraciaCore.informationSource.getInformation(searchCriteria));
    }

    public void generateWordCloud(Map<String, String> information) {
        Map<String, Integer> wordCloud = WordCloudGenerator.generate(information);
        setChanged();
        notifyObservers(wordCloud);
    }
    
    public String getInformationSourceName() {
        return noticraciaCore.informationSource.getName();
    }
}
