package com.master.spring.boot.blog.vo;

import java.io.Serializable;

import com.master.spring.boot.blog.domain.Catalog;

/**
 * 
 * Catalog VO 值对象
 * 
 * @author zhu
 *
 */
public class CatalogVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String username;
	
	private Catalog catalog;
	
	public CatalogVO() {
		super();
	}

	public CatalogVO(String username, Catalog catalog) {
		super();
		this.username = username;
		this.catalog = catalog;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
	
}
