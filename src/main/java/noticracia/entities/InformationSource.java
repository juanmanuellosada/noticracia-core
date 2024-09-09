package noticracia.entities;

import java.util.Set;

/**
 * Define la interfaz a implementar por las {@code fuentes de}  {@link noticracia.entities.Information información}.
 */
public interface InformationSource {

    /**
     * Devuelve el nombre de la {@code fuente de información}
     * @return nombre {@link String}
     */
    String getName();

    /**
     *
     * @param politicianName {@link String}.
     * @return conjunto de {@link noticracia.entities.Information información} del {@code candidato politico}.
     */
    Set<Information> getInformation(String politicianName);
}
