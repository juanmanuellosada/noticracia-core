package noticracia.entities;

import lombok.Data;

import java.util.Set;

@Data
public abstract class InformationSource {

    public String name;
    public abstract Set<Information> getInformation(String politician);
}
