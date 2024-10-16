package noticracia.entities;

import noticracia.core.NoticraciaCore;

import java.util.Map;

/**
 * Clase abstracta que representa una fuente de información. Estas fuentes de
 * información pueden ser utilizadas para obtener información acerca de un
 * candidato pol tico, ya sea desde Internet o desde un archivo local.
 *
 * @author Noticracia
 */
public abstract class InformationSource {
    /**
     * Referencia a la instancia de la clase NoticraciaCore que la esté usando.
     */
    private NoticraciaCore core;

    /**
     * Establece la instancia de la clase NoticraciaCore que la esté usando.
     *
     * @param core la instancia de la clase NoticraciaCore.
     */
    public final void setCore(NoticraciaCore core) {
        this.core = core;
    }

    /**
     * Inicia el proceso de búsqueda de información en la fuente de información.
     *
     * @param searchCriteria el parámetro de búsqueda, en nuestro caso, el
     *                         nombre del candidato político.
     */
    public abstract void start(String searchCriteria);

    /**
     * Detiene el proceso de búsqueda de información en la fuente de información.
     */
    public abstract void stop();

    /**
     * Devuelve el nombre de la fuente de información.
     *
     * @return el nombre de la fuente de información.
     */
    public abstract String getName();

    /**
     * Notifica a la instancia de la clase NoticraciaCore que la esté utilizando
     * con la información nueva obtenida.
     *
     * @param information la información nueva obtenida.
     */
    public final void notify(Map<String, String> information) {
        this.core.refreshInformation(information);
    }

    /**
     * Mapea el resultado de la búsqueda en la fuente de información a un
     * mapa de información donde la clave es el link de la información y el valor el
     * texto.
     *
     * @param result el resultado de la búsqueda,
     * @param searchCriteria el parámetro de búsqueda.
     * @return el mapa de información.
     */
    public abstract Map<String, String> mapInformation(Object result, String searchCriteria);
}
