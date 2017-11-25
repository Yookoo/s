package com.master.spring.boot.blog.service;

import java.util.List;

import com.master.spring.boot.blog.domain.Catalog;
import com.master.spring.boot.blog.domain.User;


/**
 * Catalog 接口
 * @author zhu
 *
 */
public interface CatalogService {
	
	/**
	 * 保存Catalog
	 * @param catalog
	 * @return
	 */
	Catalog saveCatalog(Catalog catalog);
	
	/**
	 * 删除Catalog
	 * @param id
	 * @return
	 */
	void removeCatalog(Long id);

	/**
	 * 根据id获取Catalog
	 * @param id
	 * @return
	 */
	Catalog getCatalogById(Long id);
	
	/**
	 * 获取Catalog列表
	 * @return
	 */
	List<Catalog> listCatalogs(User user);
}