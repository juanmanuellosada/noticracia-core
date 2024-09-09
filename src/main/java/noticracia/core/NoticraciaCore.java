package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

@SuppressWarnings("deprecation")
public class NoticraciaCore extends Observable {

    private final Set<InformationSource> informationSources;
    private final WordCloudGenerator wordCloudGenerator;

    public NoticraciaCore(Set<InformationSource> informationSources) {
        this.informationSources = informationSources;
        this.wordCloudGenerator = new WordCloudGenerator();
    }
    public Map<String, Integer> generateWorldCloud(String politician, InformationSource source) {
        Map<String, Integer> worldCloud = wordCloudGenerator.generate(politician, source);
        notifyObservers(worldCloud);
        return worldCloud;
    }

    public List<String> getInformationSourcesNames() {
        return informationSources.stream().map(InformationSource::getName).toList();
    }

    public Set<InformationSource> getInformationSources() {
        return informationSources;
    }

}