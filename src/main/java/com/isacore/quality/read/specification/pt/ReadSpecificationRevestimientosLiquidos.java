package com.isacore.quality.read.specification.pt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.isacore.quality.classes.util.ValidateCell;
import com.isacore.quality.model.Family;
import com.isacore.quality.model.LineProduction;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.model.Property;
import com.isacore.util.ReadPropertiesExcel;

public class ReadSpecificationRevestimientosLiquidos {

	public static List<Product> read(XSSFSheet sheet) {

		System.out.println(">> Name sheet:::" + sheet.getSheetName() + ":::");

		Iterator<Row> rowIterator = sheet.iterator();

		Row row;
		Row rowhead;

		row = rowIterator.next();
		rowhead = rowIterator.next();
		row = rowIterator.next();

		List<Product> prooducts = new ArrayList<>();

		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			if (ValidateCell.isEnd(row))
				break;
			else {
				Product product = new Product();
				product.setTypeProduct(ProductType.PRODUCTO_TERMINADO);

				List<Property> properties = new ArrayList<>();

				// CODISA LÍNEA DE PRODUCCIÓN
				LineProduction lp = new LineProduction(ValidateCell.validateInteger(row.getCell(4)));
				product.setLineProduction(lp);

				// VERSIÓN
				product.setReview(ValidateCell.validateString(row.getCell(6)));

				// CODISA FAMILIA
				Family f = new Family(ValidateCell.validateInteger(row.getCell(7)));
				product.setFamily(f);

				// CONCATENAR
				product.setItcdq(ValidateCell.validateString(row.getCell(13)));

				// FAMILIA ITCDQ
				product.setTypeProductTxt(ValidateCell.validateString(row.getCell(15)));

				// CÓDIGO SAP
				product.setSapCode(ValidateCell.toString(row.getCell(16)));

				// CODISA PRODUCTO
				product.setIdProduct(ValidateCell.validateInteger(row.getCell(18)));

				// PRODUCTO
				product.setNameProduct(ValidateCell.validateString(row.getCell(19)));

				// ----------------------Lectura de propiedades----------------------------
//				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 20, 20, ReadPropertiesExcel.UNIQUE);
//				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 21, 21, ReadPropertiesExcel.UNIQUE);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 22, 34, ReadPropertiesExcel.NOMINAL);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 38, 74, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 77, 77, ReadPropertiesExcel.UNIQUE);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 79, 85, ReadPropertiesExcel.STANDARD);
				
				product.setProperties(properties);
				prooducts.add(product);		
				
			}
		}
		return prooducts;
	}

}
