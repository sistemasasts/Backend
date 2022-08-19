package com.isacore.util;

import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.isacore.quality.classes.util.ValidateCell;
import com.isacore.quality.model.Property;

public class ReadPropertiesExcel {
	
	public static final String UNIQUE = "u";
	public static final String NOMINAL = "n";
	public static final String STANDARD = "s";
	
	/*
	 * @param  type u unico, n contiene nominal, s no contiene nominal
	 */
	public static List<Property> technical(List<Property> properties,Row row, Row rowHead, int start, int end, String type, String... unit) {

		//FECHA
		LocalDate fechaExcel = ValidateCell.validateLocalDate(row.getCell(0));
		
		for (int i = start; i <= end; i++) {
			Property p = new Property();			
			p.setTypeProperty(PropertyText.PROP_TECHNICAL);
			p.setDateUpdateExcel(fechaExcel);
			//id Property y norma
			p.setIdPropNorm(ValidateCell.validateString(rowHead.getCell(i)));
			
			
			p.setMinProperty(ValidateCell.validateDouble(row.getCell(i)));
			
			if(type.equals(UNIQUE)) {
				if(unit != null && unit.length > 0)
					p.setUnitProperty(unit[0]);
				else
					// unidad
					p.setUnitProperty(ValidateCell.validateString(row.getCell(++i)));
			}else {
				// Peso Max
				p.setMaxProperty(ValidateCell.validateDouble(row.getCell(++i)));
				
				if(type.equals(NOMINAL))
					++i;
					
				// unidad
				p.setUnitProperty(ValidateCell.validateString(row.getCell(++i)));				
			}
						
			if (!p.isEmpty())
				properties.add(p);

		}
		
		return properties;
	}
	
	public static List<Property> visual(List<Property> properties, Row row, Row rowHead, int start, int end){
		
		for(int i = start; i <= end; i++) {
			Property p = new Property();
			p.setTypeProperty(PropertyText.PROP_VISUAL);
			//id Property y norma
			System.out.println("Valor Concatenado**********-----> " + ValidateCell.validateString(rowHead.getCell(i)));
			p.setIdPropNorm(ValidateCell.validateString(rowHead.getCell(i)));
			
			//valor de la propiedad
			p.setViewProperty(ValidateCell.validateString(row.getCell(i)));
			
			if (!p.isEmpty())
				properties.add(p);			
		}
		
		
		return properties;
	}
	
}
