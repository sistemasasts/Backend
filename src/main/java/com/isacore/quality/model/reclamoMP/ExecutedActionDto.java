package com.isacore.quality.model.reclamoMP;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ExecutedActionDto implements Serializable {

    private long id;
    private long idReclamo;
    private String description;

    public ExecutedActionDto() {    }

}
