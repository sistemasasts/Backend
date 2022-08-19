package com.isacore.quality.read.specification.mp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.ProdProv;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Property;
import com.isacore.quality.read.specification.pt.ReadSpecificationLaminas;
import com.isacore.quality.service.impl.ProdProvServiceImpl;
import com.isacore.quality.service.impl.ProductServiceImpl;
import com.isacore.quality.service.impl.PropertyServiceImpl;

@Component
public class GeneralReadMP {

	@Autowired
	private ProductServiceImpl productService;

	@Autowired
	private PropertyServiceImpl propertyService;
	
	@Autowired
	private ProdProvServiceImpl prodProvService;

	public static final String FILE_NAME = "Especificaciones materias primas Actualizado vs1.xlsx";

	public static final String PATH_TESTS_TEMPLATE = "C:\\CRIMPTEK\\Calidad\\EspecificacionesFormulaciones\\"
			+ FILE_NAME;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void run(String user) {

		File excelFile = new File(PATH_TESTS_TEMPLATE);
		if (excelFile.exists()) {
			readEspecification(excelFile, user);
		} else {
			logger.error(">> El directorio:::" + PATH_TESTS_TEMPLATE + ":::no existe");
		}
	}

	public void readEspecification(File f, String user) {

		FileInputStream excelFile;
		XSSFWorkbook workbook = null;

		try {
			excelFile = new FileInputStream(f);

			workbook = new XSSFWorkbook(excelFile);

			List<Product> listProduct;

			listProduct = ReadSpecificationMP.read(workbook.getSheet("Especificaciones"));
			writeEspecifications(listProduct, user);
			
		} catch (IOException e) {
			logger.info(">> No se ha podido leer el libro de excel:::" + FILE_NAME);
		} finally {
			try {
				workbook.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			;
		}

	}
	
	@Transactional
	public void writeEspecifications(List<Product> listProduct, String user) {

		listProduct.forEach(x -> {

			System.out.println("Producto a guardar----> " + x.getNameProduct());
			
			List<ProdProv> listProdProv = x.getProvidersList();
			listProdProv.forEach(z -> {
				z.setProduct(x);
				Integer cod = this.prodProvService.validateProdProv(z.getProduct().getIdProduct(), z.getProvider().getIdProvider());
				if(cod == null)
					this.prodProvService.createProdProv(null);
				else
					this.prodProvService.updateProdProv(null);
			});

			Product p;
			List<Property> listProperties = x.getProperties();
			x.setProperties(null);
			//p = this.productService.create(x);

			listProperties.forEach(y -> {
				y.setProduct(x);
				y.setDateUpdate(LocalDateTime.now());
				System.out.println("********************************>>>> " + y.getPropertyList().getIdProperty());
				String propertyDescription = this.propertyService.validateExistProperty(y.getProduct().getIdProduct(),
						y.getPropertyList().getIdProperty());
				if (propertyDescription == null)
					this.propertyService.createProperty(y, user);
				else
					this.propertyService.updateProperty(y, user);
			});

		});
		
		System.out.println("================Fin de la tarea=================");

	}
}
