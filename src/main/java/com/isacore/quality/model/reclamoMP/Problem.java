package com.isacore.quality.model.reclamoMP;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity(name = "problem")
@Table(name = "PROBLEM")
public class Problem extends EntidadBase {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "PROBLEM_ID")
//    private Integer idProblem;

    @NotNull
    @Column(columnDefinition = "bigint default 0")
    private long defectoId;
    @Column(name = "PROBLEM_DESCRIPTION", nullable = true, columnDefinition = "varchar(max)")
    private String description;

    @Column(name = "PROBLEM_PICTURE", nullable = true)
    private String pictureStringB64;

    @Column(name = "PROBLEM_NAME_FILE", nullable = true)
    private String nameFileP;

    @Column(name = "PROBLEM_EXTEN_FILE", nullable = true)
    private String extensionFileP;

    protected Problem() {    }

    public Problem(long defectoId, String description, String pictureStringB64, String nameFileP, String extensionFileP) {
        this.defectoId = defectoId;
        this.description = description;
        this.pictureStringB64 = pictureStringB64;
        this.nameFileP = nameFileP;
        this.extensionFileP = extensionFileP;
    }
}
