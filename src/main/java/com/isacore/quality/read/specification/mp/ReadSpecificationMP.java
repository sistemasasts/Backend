package com.isacore.quality.read.specification.mp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.isacore.quality.classes.util.ValidateCell;
import com.isacore.quality.model.Feature;
import com.isacore.quality.model.ProdProv;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.model.Property;
import com.isacore.quality.model.Provider;
import com.isacore.quality.model.ProviderStatus;
import com.isacore.util.PropertyText;
import com.isacore.util.ReadPropertiesExcel;

public class ReadSpecificationMP {

	public static List<Product> read(XSSFSheet sheet) {

		System.out.println(">> Name sheet:::" + sheet.getSheetName() + ":::");

		Iterator<Row> rowIterator = sheet.iterator();

		Row row;
		Row rowHead;

		row = rowIterator.next();
		rowHead = rowIterator.next();
		row = rowIterator.next();

		List<Product> prooducts = new ArrayList<>();

		Integer backValue = Integer.MIN_VALUE;
		Integer codSap;

		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			if (ValidateCell.isEnd(row))
				break;
			else {

				Product product = new Product();
				

				codSap = ValidateCell.validateInteger(row.getCell(2));
				if (codSap.intValue() == backValue.intValue()) {
					
					//COD ISA PROVEEDOR
					Provider prov = new Provider(ValidateCell.validateInteger(row.getCell(25)));
									
					//STATUS					
					ProdProv prodProv = new ProdProv();
					prodProv.setProvider(prov);
					prodProv.setStatus(ProviderStatus.valueOf(ValidateCell.validateString(row.getCell(30))));
					
					//TIPO DE PROVEEDOR
					prodProv.setTypeProv(ValidateCell.validateString(row.getCell(31)));
					product.getProvidersList().add(prodProv);
					
					backValue = ValidateCell.validateInteger(row.getCell(1));

				} else {

					product.setTypeProduct(ProductType.MATERIA_PRIMA);
					
					List<Property> properties = new ArrayList<>();

					// CODIGO SAP
					product.setSapCode(ValidateCell.toString(row.getCell(1)));
					backValue = ValidateCell.validateInteger(row.getCell(1));
					
					// CODIGO ISA
					product.setIdProduct(ValidateCell.validateInteger(row.getCell(2)));

					// BODEGA
					product.setWarehouse(ValidateCell.toString(row.getCell(6)));

					// DESCRIPCIÓN DE GRUPO
					product.setTypeProductTxt(ValidateCell.validateString(row.getCell(11)));

					// ITCDQ
					product.setItcdq(ValidateCell.validateString(row.getCell(12)));

					// REVISION
					product.setReview(ValidateCell.toString(row.getCell(14)));

					// REFERENCIA DOCUMENTO
					product.setReference(ValidateCell.validateString(row.getCell(17)));

					// NOMBRE GENÉRICO
					product.setGenericName(ValidateCell.validateString(row.getCell(18)));

					// NOMBRE COMERCIAL
					product.setNameProduct(ValidateCell.validateString(row.getCell(19)));

					// Cantidad Almacenamiento
					product.setStoreQuantity(ValidateCell.validateString(row.getCell(20)));

					// DESCRIPCIÓN
					product.setDescProduct(ValidateCell.validateString(row.getCell(21)));

					// PROCEDENCIA
//					product.setOrigin(ValidateCell.validateString(row.getCell(22)));

					// USO ESPECÍFICO
					product.setSpecificUse(ValidateCell.validateString(row.getCell(23)));

					// PRESENTACIÓN
					product.setPresentation(ValidateCell.validateString(row.getCell(24)));

					//COD ISA PROVEEDOR
					Provider prov = new Provider();
					prov.setIdProvider(ValidateCell.validateInteger(row.getCell(25)));

					
					// GRUPO DE ARTÍCULOS
					product.setSapGroup(ValidateCell.toString(row.getCell(27)));

					// UNIDAD
					product.setUnit(ValidateCell.validateString(row.getCell(28)));

					// INDICADOR ABC
					product.setRegister(ValidateCell.validateString(row.getCell(29)));
					
					//STATUS
					ProdProv prodProv = new ProdProv();
					prodProv.setProvider(prov);
					prodProv.setStatus(ProviderStatus.valueOf(ValidateCell.validateString(row.getCell(30))));
					
					//TIPO DE PROVEEDOR
					prodProv.setTypeProv(ValidateCell.validateString(row.getCell(31)));
					List<ProdProv> listProdProv = new ArrayList<>();
					listProdProv.add(prodProv);
					product.setProvidersList(listProdProv);

					// INICIO DE LAS
					// ESPECIFICACIONES****************************************************************

					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 32, 188, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 191, 191);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 193, 193);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 195, 195);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 197, 227, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 230, 230);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 232, 232);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 234, 234);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 236, 296, ReadPropertiesExcel.STANDARD);
					
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 299, 299);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 301, 304, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 307, 307, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 309, 309, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 312,312);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 314,338, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 341,341);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 343,343);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 345, 345, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 347,347, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 350, 350, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 352, 352, ReadPropertiesExcel.UNIQUE);
					
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 354,372, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 375,375);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 377,377, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 380,380);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 382,397, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 400, 400, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 402,402, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 405, 405, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 407,407, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 410, 410, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 412, 412, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 414, 414, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 416, 416, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 418, 418, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 420,438, ReadPropertiesExcel.STANDARD);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 441,441);
					properties = ReadPropertiesExcel.technical(properties, row, rowHead, 443, 443, ReadPropertiesExcel.UNIQUE);
					properties = ReadPropertiesExcel.visual(properties, row, rowHead, 445,445);
					
					
					
					product.setProperties(properties);
					prooducts.add(product);
					
				}
			}
		}
		return prooducts;
	}

}
