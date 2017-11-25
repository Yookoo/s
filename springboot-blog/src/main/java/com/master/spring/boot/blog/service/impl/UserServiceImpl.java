package com.master.spring.boot.blog.service.impl;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.repository.UserRepository;
import com.master.spring.boot.blog.service.UserService;
/**
 * 用户服务接口实现
 * 
 * @author ZKY
 *
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService{

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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> listUsersByUsernames(Collection<String> usernames) {
		return userRepository.findByUsernameIn(usernames);
	}



}
