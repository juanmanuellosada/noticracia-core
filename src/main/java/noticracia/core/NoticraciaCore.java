package noticracia.core;

import noticracia.entities.InformationSource;
import noticracia.entities.InformationSourceNull;
import noticracia.services.information.discovery.InformationSourceDiscoverer;
import noticracia.services.validators.PathValidator;

public class NoticraciaCore {

    public final InformationSource informationSource;

    public NoticraciaCore(String path) {
        this.informationSource = initializeInformationSource(path);
    }

    private InformationSource initializeInformationSource(String path) {
        PathValidator.validate(path);

        Class<? extends InformationSource> informationSourceClass = new InformationSourceDiscoverer().discover(path);
        if (informationSourceClass == InformationSourceNull.class) {
            System.err.println("No information source found.");
            return new InformationSourceNull();
        }

        try {
            return informationSourceClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            System.err.println("Error instantiating information source: " + informationSourceClass.getName());
            return new InformationSourceNull();
        }
    }
}
