package com.isacore.quality.read.formulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.isacore.quality.classes.util.ValidateCell;
import com.isacore.quality.model.Formulation;
import com.isacore.util.ReadFormulationExcel;

public class ReadFormulationImpermeabilizacion {
	
	public static List<Formulation> read(XSSFSheet sheet){
		
		System.out.println(">> Name sheet:::" + sheet.getSheetName() + ":::");
		
		Iterator<Row> rowIterator = sheet.iterator();

		Row row;
		Row rowHead;

		row = rowIterator.next();
		rowHead = rowIterator.next();
		row = rowIterator.next();

		List<Formulation> listFormulation = new ArrayList<>();

		while(rowIterator.hasNext()) {
			row = rowIterator.next();
			
			if (ValidateCell.isEnd(row))
				break;
			else {
				ReadFormulationExcel.start(listFormulation, row, rowHead, 45, 56);	
				
			}
			
		}
		
		return listFormulation;		
		
	}
	
}