package noticracia.core;

import noticracia.entities.InformationSource;

import java.util.*;

/**
 * La clase NoticraciaCore se encarga de gestionar las fuentes de información,
 *
 * @author Noticracia
 */
public class NoticraciaCore {

    /**
     * La referencia a su clase intermediaria, Noticracia.
     */
    private final Noticracia noticracia;
    /**
     * El Map que guarda las diferentes fuentes de información, la llave es el nombre y el valor la fuente.
     */
    protected final Map<String, InformationSource> informationSources;
    /**
     * Guardo la referencia a la fuente de información actual.
     */
    private InformationSource currentInformationSource;
    /**
     * Guardo la última información que recibí desde la fuente de información,
     * por si me llega nueva y es igual a la anterior.
     */
    private final Map<String, String> lastSentInformation = new HashMap<>();

    /**
     * Inicializa la clase NoticraciaCore.
     *
     * @param noticracia    La clase Noticracia.
     * @param informationSources El Map que guarda las diferentes fuentes de información, la llave es el nombre
     *                           y el valor la fuente.
     */
    public NoticraciaCore(Noticracia noticracia, Map<String, InformationSource> informationSources) {
        this.noticracia = noticracia;
        this.informationSources = informationSources;
        this.informationSources.forEach((name, informationSource) -> informationSource.setCore(this));
    }

    /**
     * Inicia una búsqueda en una fuente de información en particular.
     *
     * @param informationSourceName el nombre de la fuente de información.
     * @param searchCriteria       El parámetro de búsqueda, en nuestro caso, el nombre del candidato político.
     */
    protected void startSearch(String informationSourceName, String searchCriteria) {

        validateInput(informationSourceName, searchCriteria);

        lastSentInformation.clear();

        InformationSource informationSource = getInformationSource(informationSourceName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown information source: " + informationSourceName));

        switchInformationSourceIfNeeded(informationSource);

        currentInformationSource.start(searchCriteria);
    }

    /**
     * Valida los parámetros de entrada.
     * @param informationSourceName El nombre de la fuente de información.
     * @param searchCriteria       El parámetro de búsqueda, en nuestro caso, el nombre del candidato político.
     */
    private void validateInput(String informationSourceName, String searchCriteria) {
        if (informationSourceName == null || informationSourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Information source name cannot be null or empty.");
        }
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            throw new IllegalArgumentException("Search criteria cannot be null or empty.");
        }
    }

    /**
     * Obtiene, si existe, la fuente de información que coincida con el nombre.
     * @param informationSourceName El nombre de la fuente de información.
     * @return La fuente de información que coincida con el nombre, si existe.
     */
    private Optional<InformationSource> getInformationSource(String informationSourceName) {
        return Optional.ofNullable(this.informationSources.get(informationSourceName));
    }

    /**
     * Verifica si la fuente de información actual es diferente a la nueva fuente de información y si es asi,
     *  se detiene la actual y se inicia la nueva.
     *  @param newSource La nueva fuente de información.
     */
    private void switchInformationSourceIfNeeded(InformationSource newSource) {
        if (isDifferentSource(newSource)) {
            stopCurrentInformationSource();
            currentInformationSource = newSource;
        }
    }

    /**
     * Detiene la fuente de información actual.
     */
    private void stopCurrentInformationSource() {
        if (currentInformationSource != null) {
            currentInformationSource.stop();
            currentInformationSource = null;
        }
    }

    /**
     * Genera una nube de palabras a partir de un mapa de información si hay información nueva.
     * @param information El mapa de información, la clave es el link de la información y el valor el texto
     *                    información.
     */
    public void refreshInformation(Map<String, String> information) {
        if (isNewInformation(information)) {
            lastSentInformation.putAll(information);
            noticracia.generateWordCloud(lastSentInformation);
        }
    }

    /**
     * Verifica si la fuente de información actual es diferente a la nueva fuente de información.
     * @param informationSource La nueva fuente de información.
     * @return True si la fuente de información actual es diferente a la nueva fuente de información.
     */
    private boolean isDifferentSource(InformationSource informationSource) {
        return Objects.isNull(currentInformationSource) ||
                !currentInformationSource.getName().equals(informationSource.getName());
    }

    /**
     * Verifica si hay información nueva.
     * @param information El mapa de información.
     * @return True si hay información nueva.
     */
    private boolean isNewInformation(Map<String, String> information) {
        return !isContained(information, lastSentInformation);
    }

    /**
     * Verifica si la información actual contiene la información nueva.
     * @param information La información nueva.
     * @param lastSentInformation La información actual.
     * @return True si la información actual contiene la información nueva.
     */
    private boolean isContained(Map<String, String> information, Map<String, String> lastSentInformation) {
        return information.entrySet().stream()
                .allMatch(e -> e.getValue().equals(lastSentInformation.get(e.getKey())));
    }

    /**
     * Agrega las fuentes de información nuevas.
     * @param newInformationSources Las fuentes de información nuevas.
     */
    public void addInformationSources(Map<String, InformationSource> newInformationSources) {
        newInformationSources.forEach((name, informationSource) -> {
            if (!informationSources.containsKey(name)) {
                informationSources.put(name, informationSource);
            }
        });
    }

}