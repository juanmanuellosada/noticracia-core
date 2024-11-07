package noticracia.core;

import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.*;

@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    public final NoticraciaCore noticraciaCore;
    private final WordCloudGenerator wordCloudGenerator;

    public Noticracia(NoticraciaCore noticraciaCore) {
        this.noticraciaCore = noticraciaCore;
        wordCloudGenerator = new WordCloudGenerator();
    }

    public boolean search(String searchCriteria) {
        Map<String, String> information = noticraciaCore.informationSources.values().
                iterator().next().getInformation(searchCriteria);

        if (information.isEmpty()) return false;

        notifyObservers(wordCloudGenerator.generate(information));
        return true;
    }
}
