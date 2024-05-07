package com.isacore.quality.model.reclamoMP;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
public class ProblemDto implements Serializable {

    private long id;
    private long reclamoId;
    private long defectoId;
    private String description;
    private String pictureStringB64;
    private String nameFileP;
    private String extensionFileP;
    private String base64;

}
