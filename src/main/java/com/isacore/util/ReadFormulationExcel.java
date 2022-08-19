package com.isacore.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.isacore.quality.classes.util.ValidateCell;
import com.isacore.quality.model.FormulaList;
import com.isacore.quality.model.Formulation;
import com.isacore.quality.model.FormulationItem;
import com.isacore.quality.model.Product;

public class ReadFormulationExcel {

	public static List<Formulation> start(List<Formulation> listFormulation, Row row, Row rowHead, int start, int end) {

		// FECHA
		LocalDate ld = ValidateCell.validateLocalDate(row.getCell(0));

		// COD ISA
		Product p = new Product();
		p.setIdProduct(ValidateCell.validateInteger(row.getCell(9)));
		System.out.println("Cod ISA Prod:::::: " + p.getIdProduct());

		// COD ISA Tipo de formula
		FormulaList fl = new FormulaList();
		fl.setIdFormula(ValidateCell.validateInteger(row.getCell(11)));
		System.out.println("Cod ISA Formula:::::: " + fl.getIdFormula());
		
		//Carga
		Integer load = ValidateCell.validateInteger(row.getCell(39));

		for (int i = start; i <= end; i++) {

			Double value = ValidateCell.validateDouble(row.getCell(i));

			if (value != 0) {
				Formulation f = new Formulation();
				f.setDateUpdate(LocalDateTime.now());
				f.setDateUpdateExcel(ld);
				f.setProduct(p);
				f.setFormulationList(fl);
				f.setMultiFactor(load);

				FormulationItem fi = new FormulationItem();
				fi.setIdFItem(ValidateCell.validateInteger(rowHead.getCell(i)));
				f.setFormulationItem(fi);
				System.out.println("---Cod ISA Item:::::: " + fi.getIdFItem());

				f.setValue(value);
				System.out.println("---Cod ISA Value%:::::: " + f.getValue());
				listFormulation.add(f);
			}
		}

		return listFormulation;

	}

}
