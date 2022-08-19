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

public class ReadSpecificationViales {
	
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
				
				//CODISA LÍNEA DE PRODUCCIÓN
				LineProduction lp = new LineProduction(ValidateCell.validateInteger(row.getCell(4)));
				product.setLineProduction(lp);
				
				//VERSIÓN
				product.setReview(ValidateCell.validateString(row.getCell(6)));
				
				//CODISA FAMILIA
				Family f = new Family(ValidateCell.validateInteger(row.getCell(7)));
				product.setFamily(f);

				// CONCATENAR
				product.setItcdq(ValidateCell.validateString(row.getCell(13)));

				// FAMILIA ITCDQ
				product.setTypeProductTxt(ValidateCell.validateString(row.getCell(14)));

				// CÓDIGO SAP
				product.setSapCode(ValidateCell.toString(row.getCell(15)));

				// CODISA PRODUCTO
				product.setIdProduct(ValidateCell.validateInteger(row.getCell(16)));

				// PRODUCTO
				product.setNameProduct(ValidateCell.validateString(row.getCell(17)));

				// ----------------------Lectura de propiedades----------------------------
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 18, 21, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 24, 24, ReadPropertiesExcel.UNIQUE, "");
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 25, 73, ReadPropertiesExcel.STANDARD);
				
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 76, 79, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 82, 82, ReadPropertiesExcel.UNIQUE);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 84, 102, ReadPropertiesExcel.STANDARD);
				
				product.setProperties(properties);
				prooducts.add(product);		
				
			}
		}
		return prooducts;
	}
	
}
