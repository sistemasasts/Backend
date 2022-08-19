package com.isacore.sgc.acta.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "menu")
@Table(name = "MENU")
public class Menu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEN_IDMENU")
	private int idMenu;
	
	@Column(name = "MEN_ICON", nullable = false, length = 128)
	private String iconMenu;
	
	@Column(name = "MEN_ITEMDESC", nullable = false, length = 128)
	private String itemDescription;
	
	@Column(name = "MEN_REF", nullable = true)
	private String ref;
	
	@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "menus")
	private List<Role> Roles;
	//coloco LAZY, pero aun asi me salen los submenus
	@OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "MEN_IDMENU", nullable = false)
	private List<SubMenu> subMenus;

	public int getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public String getIconMenu() {
		return iconMenu;
	}

	public void setIconMenu(String iconMenu) {
		this.iconMenu = iconMenu;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public List<SubMenu> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<SubMenu> subMenus) {
		this.subMenus = subMenus;
	}
	
	
	
}
