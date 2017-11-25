package com.master.spring.boot.blog.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.domain.es.Esblog;
import com.master.spring.boot.blog.vo.TagVO;
/**
 * Esblog 服务接口
 * @author zhu
 *
 */
public interface EsblogService {

	/**
	 * 删除Esblog
	 * @param id
	 * @return
	 */
	void removeEsblog(String id);
	
	/**
	 * 更新 Esblog
	 * @param Esblog
	 * @return
	 */
	Esblog updateEsblog(Esblog esblog);
	
	/**
	 * 根据Blog的Id获取Esblog
	 * @param id
	 * @return
	 */
	Esblog getEsblogByBlogId(Long blogId);

	/**
	 * 最新博客列表，分页
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	Page<Esblog> listNewEsblogs(String keyword, Pageable pageable);
 
	/**
	 * 最热博客列表，分页
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	Page<Esblog> listHotEsblogs(String keyword, Pageable pageable);
	
	/**
	 * 博客列表，分页
	 * @param pageable
	 * @return
	 */
	Page<Esblog> listEsblogs(Pageable pageable);
	/**
	 * 最新前5
	 * @param keyword
	 * @return
	 */
	List<Esblog> listTop5NewEsblogs();
	
	/**
	 * 最热前5
	 * @param keyword
	 * @return
	 */
	List<Esblog> listTop5HotEsblogs();
	
	/**
	 * 最热前 30 标签
	 * @return
	 */
	List<TagVO> listTop30Tags();

	/**
	 * 最热前12用户
	 * @return
	 */
	List<User> listTop12Users();
}
