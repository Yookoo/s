package com.master.spring.boot.blog.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.repository.UserRepository;
/**
 * 用户服务接口实现
 * 
 * @author ZKY
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public User login(User user) {
		return userRepository.save(user);
	}
	
	@Transactional
	@Override
	public User register(User user) {
		return userRepository.save(user);
	}
	@Transactional
	@Override
	public User saveOrUpdate(User user) {
		return userRepository.save(user);
	}
	
	
	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public Page<User> findByNameLike(String name, Pageable pageable) {
		name = "%" + name + "%";
		return userRepository.findByNameLike(name, pageable);
	}
	
	@Transactional
	@Override
	public void deleteById(Long id) {
		userRepository.delete(id);
	}



}
