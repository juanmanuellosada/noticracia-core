package noticracia.core;

import lombok.Getter;
import noticracia.entities.Information;
import noticracia.entities.InformationSource;
import noticracia.services.worldCloud.WordCloudGenerator;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

@SuppressWarnings("deprecation")
public class NoticraciaCore extends Observable {

    @Getter
    private final Set<InformationSource> informationSources;
    private final WordCloudGenerator wordCloudGenerator = new WordCloudGenerator();

    public NoticraciaCore(Set<InformationSource> informationSources) {
        this.informationSources = informationSources;
    }
    public Map<String, Integer> generateWorldCloud(String politician, InformationSource source) {
        Map<String, Integer> worldCloud = wordCloudGenerator.generateWordCloud(politician, source);
        notifyObservers(worldCloud);
        return worldCloud;
    }

    public List<String> getInformationSourcesNames() {
        return informationSources.stream().map(InformationSource::getName).toList();
    }
}