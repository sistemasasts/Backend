package com.isacore.quality.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.isacore.quality.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.dto.ProductPropertyDTO;
import com.isacore.quality.repository.IPropertyListRepo;
import com.isacore.quality.repository.IPropertyRepo;
import com.isacore.quality.service.IPropertyService;

@Service
public class PropertyServiceImpl implements IPropertyService {

    @Autowired
    private IPropertyRepo propertyRepo;

    @Autowired
    private IPropertyListRepo propertyListRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Property> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property create(Property prop) {
        return this.propertyRepo.save(prop);
    }

    @Override
    public Property findById(Property id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property update(Property obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(String id) {
        return true;

    }

    @Override
    public String validateExistProperty(Integer idProduct, String idPropertyList) {

        List<Object> list = this.propertyRepo.validateExistProperty(idProduct, idPropertyList);

        if (list == null || list.isEmpty())
            return null;
        else
            return (String) list.get(0);

    }

    @Override
    public int createProperty(Property p, String user) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateUpdate = now.format(formatter);
        String normas = actualizarNormasPropiedad(p.getPropertyList().getIdProperty());
        Long unidad = p.getUnit() != null ? p.getUnit().getId() : null;
        if (unidad == null) {
            this.propertyRepo.createPropertyNoUnit(p.getProduct().getIdProduct(), p.getPropertyList().getIdProperty(), p.getMinProperty(), p.getMaxProperty(), p.getViewProperty(), dateUpdate, p.getTypeProperty(), normas, user);
        } else {
            this.propertyRepo.createProperty(p.getProduct().getIdProduct(), p.getPropertyList().getIdProperty(), p.getMinProperty(), p.getMaxProperty(), unidad, p.getViewProperty(), dateUpdate, p.getTypeProperty(), normas, user);
        }
        return 0;
    }

    @Override
    public int updateProperty(Property p, String user) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateUpdate = now.format(formatter);
        String normas = actualizarNormasPropiedad(p.getPropertyList().getIdProperty());
        Long unidad = p.getUnit() != null ? p.getUnit().getId() : null;
        if (unidad == null) {
            this.propertyRepo.updatePropertyNoUnit(p.getMinProperty(), p.getMaxProperty(), p.getViewProperty(), dateUpdate, p.getTypeProperty(), normas, user, p.getProduct().getIdProduct(), p.getPropertyList().getIdProperty());
        } else {
            this.propertyRepo.updateProperty(p.getMinProperty(), p.getMaxProperty(), unidad, p.getViewProperty(), dateUpdate, p.getTypeProperty(), normas, user, p.getProduct().getIdProduct(), p.getPropertyList().getIdProperty());
        }
        return 0;
    }

    private String actualizarNormasPropiedad(String idPropiedad) {
        Optional<PropertyList> propOp = propertyListRepo.findById(idPropiedad);
        if (propOp.isPresent()) {
            return propOp.get().getNorms().stream().map(x -> x.getLaboratoryNorm().getName()).collect(Collectors.joining("/"));
        }

        return "";
    }

    @Override
    public Property findByIdProductandIdProperty(Product p, PropertyList pl) {
        return this.propertyRepo.findPropertyByIdPropertyListandIdProduct(p.getIdProduct(), pl.getIdProperty());

    }

    @Override
    public void updatePropertiesByProduct(List<ProductPropertyDTO> listProperty) {


        listProperty.forEach(x -> {
            Product producto = new Product();
            producto.setIdProduct(x.getIdProduct());
            PropertyList propiedad = new PropertyList();
            propiedad.setIdProperty(x.getIdPropertyList());
            UnidadMedida unidad = null;
            if (x.getUnitId() > 0) {
                unidad = new UnidadMedida();
                unidad.setId(x.getUnitId());
            }
            Property especificacion = new Property(producto,
                    propiedad,
                    unidad,
                    x.getMinProperty(),
                    x.getMaxProperty(),
                    x.getPropertyNorm(),
                    x.getTypeProperty(),
                    x.getViewProperty(),
                    x.getAsUser());

            switch (x.getAction().toUpperCase()) {
                case "CREATE":
                    logger.info("Agregando la especificación: " + x.toString());
                    createProperty(especificacion, x.getAsUser());
                    break;
                case "UPDATE":
                    logger.info("Guardando la especificación: " + x.toString());
                    updateProperty(especificacion, x.getAsUser());
                    break;
                case "DELETE":
                    logger.info("Eliminando la especificación: " + x.toString());
                    propertyRepo.delete(especificacion);
                    break;
                default:
                    break;
            }

        });

    }

    @Override
    public List<ProductPropertyDTO> findByProduct(Product product) {

        logger.info("Obteniendo las propiedades y sus especifiaciones del producto: " + product.toString());

        List<Property> properties = propertyRepo.findByProductOrderByPropertyList_NamePropertyAsc(product);

        logger.info("Propiedades obtenidas: " + properties.size());

        return properties.stream().map(x -> new ProductPropertyDTO(
                x.getProduct().getIdProduct(),
                x.getPropertyList().getIdProperty(),
                x.getPropertyList().getNameProperty(),
                x.getMinProperty(),
                x.getMaxProperty(),
                x.getUnitProperty(),
                x.getUnit() != null ? x.getUnit().getId() : 0,
                x.getPropertyNorm(),
                x.getAsUser(),
                x.getTypeProperty(),
                x.getViewProperty()))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByProductAndProperty(Integer idProduct, String IdPropertyList) {
        try {
            Product product = new Product();
            product.setIdProduct(idProduct);
            PropertyList propiedad = new PropertyList();
            propiedad.setIdProperty(IdPropertyList);
            PropertyPK pk = new PropertyPK();
            pk.setProduct(product);
            pk.setPropertyList(propiedad);

            logger.info("Eliminando la especificación: " + IdPropertyList);

            this.propertyRepo.deleteByProductAndProperty(idProduct, IdPropertyList);
            return 0;
        } catch (Exception ex) {
            logger.info("Error: " + ex.getMessage());
            return 1;
        }

    }

    @Override
    public List<PropertyList> findPropertyAssignNot(Product product) {

        logger.info("Buscar propiedades asignadas del producto:" + product.toString());

        List<Property> especificaciones = this.propertyRepo.findByProductOrderByPropertyList_NamePropertyAsc(product);

        logger.info("Propiedades asignadas :" + especificaciones.size());

        List<String> idsPropiedadesAsignadas = new ArrayList<>();
        especificaciones.forEach(x -> {
            idsPropiedadesAsignadas.add(x.getPropertyList().getIdProperty());
        });

        List<PropertyList> propidadesNoAsignadas = new ArrayList<>();

        if (idsPropiedadesAsignadas.isEmpty())
            propidadesNoAsignadas = this.propertyListRepo.findAll();
        else
            propidadesNoAsignadas = this.propertyListRepo.findByIdPropertyNotIn(idsPropiedadesAsignadas);

        logger.info("Propiedades no asignadas :" + propidadesNoAsignadas.size());

        return propidadesNoAsignadas;
    }

    @Override
    @Transactional
    public void apdateNormsPropertiesByPropertyListId(String propertuListId, String norms) {

        List<Property> properties = propertyRepo.findByPropertyList_IdProperty(propertuListId);
        properties.forEach(x -> x.setPropertyNorm(norms));

    }

}
