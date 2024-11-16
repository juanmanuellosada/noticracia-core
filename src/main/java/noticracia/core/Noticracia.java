package noticracia.core;

import noticracia.entities.WordCloud;
import noticracia.services.updates.UpdateScheduler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase principal que representa el sistema Noticracia.
 * Esta clase gestiona el procesamiento de información desde diferentes fuentes,
 * la generación de nubes de palabras y la programación de actualizaciones.
 * Utiliza el patrón Observer para notificar a los observadores cuando se generan
 * nuevas nubes de palabras.
 * @author Noticracia
 */
@SuppressWarnings("deprecation")
public class Noticracia extends Observable {

    public final NoticraciaCore noticraciaCore;
    private final Set<String> processedInformation;
    private UpdateScheduler updateScheduler;
    private WordCloud currentWordCloud;

    public Noticracia(NoticraciaCore noticraciaCore) {
        this.noticraciaCore = noticraciaCore;
        this.processedInformation = new HashSet<>();
    }

    public WordCloud generateWordCloud(String politicalCandidate, String informationSourceName) {
        Set<String> newInformation = noticraciaCore.getInformation(politicalCandidate, informationSourceName);
        Set<String> unprocessedInformation = filterUnprocessedInformation(newInformation);

        if (!unprocessedInformation.isEmpty()) {
            currentWordCloud = new WordCloud(unprocessedInformation);
            processedInformation.addAll(unprocessedInformation);
            setChanged();
            notifyObservers(currentWordCloud);
        }

        return currentWordCloud;
    }

    public WordCloud generateAndStartUpdating(String politicalCandidate, String informationSourceName, long interval) {
        WordCloud wordCloud = generateWordCloud(politicalCandidate, informationSourceName);
        startUpdating(politicalCandidate, informationSourceName, interval);
        return wordCloud;
    }

    public void startUpdating(String politicalCandidate, String informationSourceName, long interval) {
        stopUpdating();
        updateScheduler = new UpdateScheduler(this, politicalCandidate, informationSourceName, interval);
        updateScheduler.start();
    }

    public void stopUpdating() {
        if (updateScheduler != null) {
            updateScheduler.stop();
            updateScheduler = null;
            processedInformation.clear();
            currentWordCloud = null;
        }
    }

    public void updateObservers(String politicalCandidate, String informationSourceName) {
        Set<String> newInformation = noticraciaCore.getInformation(politicalCandidate, informationSourceName);
        Set<String> unprocessedInformation = filterUnprocessedInformation(newInformation);

        if (!unprocessedInformation.isEmpty()) {
            currentWordCloud = new WordCloud(unprocessedInformation);
            processedInformation.addAll(unprocessedInformation);
            setChanged();
            notifyObservers(currentWordCloud);
        }
    }

    private Set<String> filterUnprocessedInformation(Set<String> information) {
        return information.stream()
                .filter(data -> !processedInformation.contains(data))
                .collect(Collectors.toSet());
    }
}