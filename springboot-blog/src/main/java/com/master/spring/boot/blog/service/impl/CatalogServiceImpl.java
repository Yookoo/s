package com.master.spring.boot.blog.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.Catalog;
import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.repository.CatalogRepository;
import com.master.spring.boot.blog.service.CatalogService;

/**
 * 
 * Catalog Service 实现类
 * @author zhu
 *
 */

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private CatalogRepository catalogRepository;
	
	/**
	 * 保存
	 */
	@Transactional
	@Override
	public Catalog saveCatalog(Catalog catalog) {
		List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
		
		if( list != null && list.size()>0){
			throw new IllegalArgumentException("该分类已经存在");
		}
		return catalogRepository.save(catalog);
	}

	/**
	 * 删除
	 */
	@Transactional
	@Override
	public void removeCatalog(Long id) {
		catalogRepository.delete(id);
	}
	/**
	 * 根据id查询
	 */
	@Override
	public Catalog getCatalogById(Long id) {
		return catalogRepository.findOne(id);
	}

	@Override
	public List<Catalog> listCatalogs(User user) {
		return catalogRepository.findByUser(user);
	}

}
