package com.waylau.spring.boot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.waylau.spring.boot.domain.User;

/**
 * 用户  资源库
 * 
 * @author ZKY
 *
 */
public interface UserRepository extends CrudRepository<User, Long>{

	/**
	 * 保存或更新
	 * @param user
	 */
	//public void saveOrUpdate(User user);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	//public User findById(Long id);
	/**
	 * 查询所有
	 * @return
	 */
	//public List<User> findAll();
	/**
	 * 根据ID删除
	 * @param id
	 */
	//public void deleteById(Long id);
	
	
	
	
}
