package com.master.spring.boot.blog.vo;
/**
 * 后台管理的菜单
 * 
 * @author ZKY
 *
 */
public class Menu {

	private String name;
	private String url;
	
	
	protected Menu() {
		super();
	}
	public Menu(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
