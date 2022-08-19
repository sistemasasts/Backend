package com.isacore.sgc.acta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "subMenu")
@Table(name = "SUBMENU")
public class SubMenu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUB_IDSUBMENU", nullable = false)
	private int idSubMenu;
	
	@Column(name = "SUB_ICON", nullable = false, length = 128)
	private String icon;
	
	@Column(name = "SUB_ITEMDESC", nullable = false, length = 128)
	private String desc;
	
	@Column(name = "SUB_REF", nullable = false, columnDefinition = "varchar(Max)")
	private String ref;

	public int getIdSubMenu() {
		return idSubMenu;
	}

	public void setIdSubMenu(int idSubMenu) {
		this.idSubMenu = idSubMenu;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	
}
