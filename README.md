# Noticracia

```mermaid
classDiagram
    class Noticracia {
        -InformationManager informationManager
        +Noticracia(String path)
        +setQuery(InformationSource, String)
        +receiveWordCloud(Map~String, Integer~)
    }

    class InformationSource {
        -InformationManager informationManager
        +InformationSource(InformationManager)
        +startInformationCollection(String)
        +processQuery(String) Map~String, String~
        +getName() String
        -postProcess(Map~String, String~)
    }

    class InformationSourceFactory {
        -InformationSourceDiscoverer discoverer
        +createInformationSources(String path) Set~InformationSource~
    }

    class InformationSourceDiscoverer {
        -Set~Class<? extends InformationSource>~ classes
        +discover(String) Set~Class<? extends InformationSource>~
        +loadJarFiles(String) File[]
        +processJarFile(File)
        +processEntries(JarFile, URLClassLoader)
        +loadClass(String, URLClassLoader)
    }

    class InformationManager {
        -Set~InformationSource~ informationSources
        -Map~String, String~ lastSentInformation
        -Noticracia noticracia
        +InformationManager(Set~InformationSource~, Noticracia)
        +startInformationCollection(InformationSource, String query)
        +refreshInformation(Map~String, String~)
        +getInformationSourcesNames() Set~String~
    }

    class WordCloud {
        +generate(Map~String, Integer~) Map~String, String~
    }

    Noticracia --> InformationManager : Uses
    InformationManager --> InformationSource : Manages
    InformationSource *-- InformationManager : "Dependency"
    InformationSourceFactory --> InformationSourceDiscoverer : Uses
    InformationSourceFactory --> InformationSource : Creates
    InformationManager --> WordCloud : Uses for generating word clouds
    InformationSource --> Noticracia : Notifies for word clouds
```

```mermaid
participant UI as User Interface
participant Controller as UI Controller
participant Noticracia
participant InfoMgr as InformationManager
participant InfoSrc as InformationSource
participant WordCloud

UI->>+Controller: User selects candidate and query
Controller->>+Noticracia: setQuery(InformationSource, query)
Noticracia->>+InfoMgr: startInformationCollection(InformationSource, query)
InfoMgr->>+InfoSrc: startInformationCollection(query)
activate InfoSrc
InfoSrc->>InfoSrc: processQuery(query)
deactivate InfoSrc
InfoSrc->>-InfoMgr: postProcess(information)
InfoMgr->>+WordCloud: generate(information)
WordCloud-->>-InfoMgr: return wordCloud
InfoMgr-->>-Noticracia: receiveWordCloud(wordCloud)
Noticracia-->>-UI: displayWordCloud(wordCloud)
```