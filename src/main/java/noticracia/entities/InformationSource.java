package noticracia.entities;

import java.util.Set;

/**
 * Define la interfaz a implementar por las {@code fuentes de}  {@link noticracia.entities.Information información}.
 */
public interface InformationSource {

    /**
     * @return Nombre de la {@code fuente de información}.
     */
    String getName();

    /**
     *
     * @param politicianName nombre del {@code candidato politico}.
     * @return conjunto de {@link noticracia.entities.Information información} del {@code candidato politico}.
     */
    Set<Information> getInformation(String politicianName);
}
