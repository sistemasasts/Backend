package com.isacore.quality.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProveedorDto implements Serializable {
    private int idProvider;
    private String nameProvider;
    private String descProvider;
    private String typeProvider;
}
