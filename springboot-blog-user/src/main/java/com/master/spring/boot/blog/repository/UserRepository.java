package com.master.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.master.spring.boot.blog.domain.User;

/**
 * 用户  资源库
 * 
 * @author ZKY
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{

	/**
	 * 根据用户姓名分页模糊搜索
	 * 
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<User> findByNameLike(String name ,Pageable pageable);
	
	/**
	 * 根据帐号查找
	 * 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
	
}
