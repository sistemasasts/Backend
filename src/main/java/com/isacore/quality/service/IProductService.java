package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.dto.PatronImgenDto;
import com.isacore.quality.dto.ProductDto;
import com.isacore.quality.dto.ProductoDto;
import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.model.Product;
import com.isacore.util.CRUD;

public interface IProductService extends CRUD<Product> {

	Product findOnlyProductById(Product p);

	Product findProductByIdAndPeriod(Integer idP, String period);

	Product findProductFeature(Integer idP);

	List<Product> findAllProducts();

	void saveProductProperty(List<Product> listProduct, String user);

	ProductDto findProductByIdAndIdProperty(Integer idP, String idProperty);

	Product findProductPropertiesByIdProduct(Integer idP);

	Product listById(Integer id);

	Product modify(Product product);
	
	List<InformationAditional> createInformationAditional(Integer productId, InformationAditional criteria);

	byte[] generateReport(Integer id);
	
	String updateReview(Integer productId);

	List<ProductoDto> listarPorNombreCriterio(String nombre);

	void subirImagenPatron(Integer productoId, byte[] file, String nombreArchivo, String tipo);

	PatronImgenDto obtenerImagenPatron(Integer productoId);
}
