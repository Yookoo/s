package com.master.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.Authority;
import com.master.spring.boot.blog.repository.AuthorityRepository;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Override
	public Authority findById(Long id) {	
		return authorityRepository.findOne(id);
	}

}
