package com.isacore.quality.dto;

import java.util.ArrayList;
import java.util.List;

import com.isacore.quality.model.ProcessTestRequest;

public class ReportProcessTestRequestDto {

	private ProcessTestRequest processTest;
	
	private String[] OpcionesMotivos= {"Desarrollo Proveedores","Desarrollo Materias Primas", "Desarrollo Productos","Reclamos Clientes", "Reingeniería",  "Reducción Costos", "Mejora de Producto", "Mejora del Proceso", "Verificación de Equipos"};
	
	private String[] OpcionesMaterialDesc= {"Materia Prima","Láminas Impermeabilizantes", "Prod. en Proceso", "Prod. Terminado", "Suministros", "Accesorios", "Prod. Viales", "Rev. Líquidos", "Pinturas","Prod. Metálicos", "Paneles PUR"}; 
	
	private List<String> oMotivosSelected= new ArrayList<>();
	
	private List<String> oMaterialDescSelected = new ArrayList<>() ;
	
	private String OtroMotivo="";
	
	private String OtroMaterialDec="";
	
	private String otherObjective;
	
	private String otherMaterialDesc;

	public ProcessTestRequest getProcessTest() {
		return processTest;
	}

	public void setProcessTest(ProcessTestRequest processTest) {
		this.processTest = processTest;
	}

	public String[] getOpcionesMotivos() {
		return OpcionesMotivos;
	}

	public void setOpcionesMotivos(String[] opcionesMotivos) {
		OpcionesMotivos = opcionesMotivos;
	}

	public String[] getOpcionesMaterialDesc() {
		return OpcionesMaterialDesc;
	}

	public void setOpcionesMaterialDesc(String[] opcionesMaterialDesc) {
		OpcionesMaterialDesc = opcionesMaterialDesc;
	}

	
	public List<String> getoMotivosSelected() {
		return oMotivosSelected;
	}

	public void setoMotivosSelected(List<String> oMotivosSelected) {
		this.oMotivosSelected = oMotivosSelected;
	}

	public List<String> getoMaterialDescSelected() {
		return oMaterialDescSelected;
	}

	public void setoMaterialDescSelected(List<String> oMaterialDescSelected) {
		this.oMaterialDescSelected = oMaterialDescSelected;
	}
	
	
	
	public String getOtherObjective() {
		return otherObjective;
	}

	public void setOtherObjective(String otherObjective) {
		this.otherObjective = otherObjective;
	}

	public String getOtherMaterialDesc() {
		return otherMaterialDesc;
	}

	public void setOtherMaterialDesc(String otherMaterialDesc) {
		this.otherMaterialDesc = otherMaterialDesc;
	}

	public String getOtroMotivo() {
		return OtroMotivo;
	}

	public void setOtroMotivo(String otroMotivo) {
		OtroMotivo = otroMotivo;
	}

	public String getOtroMaterialDec() {
		return OtroMaterialDec;
	}

	public void setOtroMaterialDec(String otroMaterialDec) {
		OtroMaterialDec = otroMaterialDec;
	}

	public void motivosForReportPrint(){
		String [] motivosSelected= this.processTest.getObjective().split(",");
		Boolean flag1=false;
		for(String m: this.OpcionesMotivos) {
			flag1=false;
			for(String mS: motivosSelected) {
				if(m.equalsIgnoreCase(mS)){
					this.oMotivosSelected.add("x"+"  "+ m);
					flag1= true;
				}
			}
			if(!flag1)
				this.oMotivosSelected.add("    "+ m);
			
			
		}
		this.findOtherObjective(motivosSelected[motivosSelected.length-1]);
		
	}
	
	public void materialDescForReportPrint(){
		String [] materialDescSelected= this.processTest.getMaterialLine().split(",");
		
		Boolean flag1=false;
		for(String m: this.OpcionesMaterialDesc) {
			flag1=false;
			for(String mS: materialDescSelected) {
				if(m.equalsIgnoreCase(mS)){
					this.oMaterialDescSelected.add("x"+"  "+ m);
					flag1= true;
				}
			}
			if(!flag1)
				this.oMaterialDescSelected.add("    "+ m);
			
		}
		
		this.findOtherMaterialDesc(materialDescSelected[materialDescSelected.length-1]);;
	}
	
	private void findOtherObjective(String option) {
		Boolean flag=false;
		for(String x: this.OpcionesMotivos) {
			if(x.equalsIgnoreCase(option)) {
				flag=true;
			}
		}
		if(!flag)
			this.OtroMotivo=option;
	}
	
	private void findOtherMaterialDesc(String option) {
		Boolean flag=false;
		for(String x: this.OpcionesMaterialDesc) {
			if(x.equalsIgnoreCase(option)) {
				flag=true;
			}
		}
		if(!flag)
			this.OtroMaterialDec=option;
	}
	
}
