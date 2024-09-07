# Noticracia

## Clases

### `NoticraciaCore`

Clase del modelo, tiene lo necesario para generar la nube de palabras. Tiene el atributo `Set<InformationSource> informationSources` que son todas las fuentes de información sobre las que puede generar una nube de palabras. También tiene el atributo `WordCloudGenerator` que es el generador de nubes de palabras.

El método `generateWorldCloud` es el encargado de devolver la nube de palabras armada en base a un nombre de candidato político dado, y una fuente de información dada.

### `Information`

Corresponde a la representación de lo que se encuentra en las fuentes de información. Contiene el parámetro texto que tendrá el valor de lo que se haya recolectado de la fuente de información. El parámetro `text` es el que incluirá el nombre del candidato político, de este modo sabremos que la información recolectada hablará del mismo. En el parámetro `link` contendrá una dirección web de la fuente directa de esta información.

### `InformationSource`

Es la clase que representa la fuente de información. Tiene la capacidad de obtener información sobre un político. Esta clase será abstracta de modo que la extienda cada _plugin_ que se use en el sistema. Definiendo el método abstracto `getInformation` me aseguro que todas sus extensiones lo implementen, de modo que todas encuentren, a su forma, información sobre el nombre del candidato político dado.

### `InformationSourceDiscoverer`

Esta será la clase encargada de encontrar implementaciones de `InformationSource` . Se le pasará al método `discover` como parámetro la ruta donde tiene que buscar implementaciones de `InformationSource` y el método devolverá instanciado un `Set<InformationSource>` listo para usarse.

### `NoticraciaFactory`

Esta clase es la encargada de fabricar “cores” de Noticracia listos para usarse. Mediante el método `create` y pasándole la ruta adonde buscar fuentes de información, devolverá un `NoticraciaCore` instanciado con todas las implementaciones de `InformationSource` que encuentre en esa ruta.

## Diagrama de clases

```mermaid
classDiagram
    class NoticraciaCore {
        -Set~InformationSource~ informationSources
        -WordCloudGenerator wordCloudGenerator
        +generateWorldCloud(String, InformationSource) Map~String, Integer~
        +getInformationSourcesNames() List~String~
    }
    class InformationSource {
        -String name
        +getInformation(String) Set~Information~
    }
    class InformationSourceDiscoverer {
        +discover(String) Set~InformationSource~
    }
    class NoticraciaFactory {
        -InformationSourceDiscoverer informationSourceDiscoverer
        +create(String) NoticraciaCore
    }
    class WordCloudGenerator {
        +generateWordCloud(String, InformationSource) Map~String, Integer~
    }

    NoticraciaCore "1" -- "*" InformationSource
    NoticraciaCore "1" -- "1" WordCloudGenerator
    NoticraciaFactory "1" -- "1" InformationSourceDiscoverer
    NoticraciaFactory "1" -- "1" NoticraciaCore
    InformationSource <|-- InformationSourceDiscoverer

```
