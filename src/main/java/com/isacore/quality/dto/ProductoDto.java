package com.isacore.quality.dto;

import com.isacore.quality.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoDto implements Serializable {
    private int idProduct;
    private String nameProduct;
    private String genericName;
    private String descProduct;
    private ProductType typeProduct;


}
