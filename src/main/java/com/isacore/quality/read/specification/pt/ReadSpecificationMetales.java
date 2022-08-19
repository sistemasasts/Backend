package com.isacore.quality.read.specification.pt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.isacore.quality.classes.util.ValidateCell;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.model.Property;
import com.isacore.util.ReadPropertiesExcel;

public class ReadSpecificationMetales {

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

				// CONCATENAR
				product.setItcdq(ValidateCell.validateString(row.getCell(4)));

				// FAMILIA ITCDQ
				product.setTypeProductTxt(ValidateCell.validateString(row.getCell(5)));

				// CÓDIGO SAP
				product.setSapCode(ValidateCell.toString(row.getCell(6)));

				// CODISA PRODUCTO
				product.setIdProduct(ValidateCell.validateInteger(row.getCell(7)));

				// PRODUCTO
				product.setNameProduct(ValidateCell.validateString(row.getCell(8)));

				// ----------------------Lectura de propiedades----------------------------
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 9, 9, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 12, 28, ReadPropertiesExcel.NOMINAL);
				
				product.setProperties(properties);
				prooducts.add(product);		
				
			}
		}
		return prooducts;
	}

}
