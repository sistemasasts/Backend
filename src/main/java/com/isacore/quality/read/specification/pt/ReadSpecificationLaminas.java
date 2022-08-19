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

public class ReadSpecificationLaminas {

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
			
			if(ValidateCell.isEnd(row))
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
				
				//CONCATENAR
				product.setItcdq(ValidateCell.validateString(row.getCell(13)));
				
				//FAMILIA ITCDQ
				product.setTypeProductTxt(ValidateCell.validateString(row.getCell(14)));
				
				//CÓDIGO SAP
				product.setSapCode(ValidateCell.toString(row.getCell(15)));

				//CODISA PRODUCTO
				product.setIdProduct(ValidateCell.validateInteger(row.getCell(17)));
				
				//PRODUCTO 
				product.setNameProduct(ValidateCell.validateString(row.getCell(18)));
				
				//----------------------Lectura de propiedades----------------------------			
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 20, 24, ReadPropertiesExcel.NOMINAL);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 28, 28, ReadPropertiesExcel.UNIQUE, "m3");
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 29, 29, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 32, 44, ReadPropertiesExcel.NOMINAL);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 48, 51, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 54, 54);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 55, 61, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 64, 64);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 65, 71, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 74, 74);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 75, 105, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 108, 111);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 112, 112, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 115, 115);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 116, 116, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 119, 119);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 120, 123, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 126, 126);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 127, 127, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 130, 130, ReadPropertiesExcel.UNIQUE, "°C");
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 131, 134, ReadPropertiesExcel.STANDARD);
				properties = ReadPropertiesExcel.visual(properties, row, rowhead, 137, 138);
				properties = ReadPropertiesExcel.technical(properties, row, rowhead, 139, 139, ReadPropertiesExcel.STANDARD);
			
				product.setProperties(properties);
				prooducts.add(product);
			}

		}

		return prooducts;
	}

}
