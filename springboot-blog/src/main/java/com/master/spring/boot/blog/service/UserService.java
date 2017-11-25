package com.master.spring.boot.blog.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.master.spring.boot.blog.domain.User;

/**
 * 用户服务接口
 * 
 * @author ZKY
 *
 */
public interface UserService {

	/**
	 * 登录
	 * @param user
	 */
	User login(User user);
	
	
	/**
	 * 注册
	 * @param user
	 */
	User register(User user);
	
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	User findById(Long id);
	
	
	/**
	 * 根据姓名模糊分页查询
	 * 
	 * @param name
	 * @param pageable
	 * @return
	 */

	Page<User> findByNameLike(String name, Pageable pageable);
	
	/**
	 * 根据ID删除
	 * @param id
	 */
	void deleteById(Long id);
	
	/**
	 * 保存或更新
	 * @param user
	 */
	User saveOrUpdate(User user);

	/**
	 * 根据用户名列表查询用户列表
	 * @param usernamelist
	 * @return
	 */
	List<User> listUsersByUsernames(Collection<String> usernamelist);

}
