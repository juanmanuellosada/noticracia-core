package noticracia.entities;

import java.util.Set;

/**
 * Interfaz que representa una fuente de información en Noticracia.
 * Las clases que implementen esta interfaz deben proporcionar una forma de
 * recuperar información para candidatos políticos específicos y un nombre
 * único para la fuente.
 * @author Noticracia
 */
public interface InformationSource {

    public Set<String> getInformation(String politicalCandidate);

    public String getName();
}
