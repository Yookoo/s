package com.master.spring.boot.blog.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.Vote;
import com.master.spring.boot.blog.repository.VoteRepository;
import com.master.spring.boot.blog.service.VoteService;

/**
 * 
 * 点赞 业务 实现类
 * 
 * @author zhu
 *
 */

@Service
public class VoteServiceImpl implements VoteService {
	
	@Autowired
	private VoteRepository voteRepository;

	/**
	 * 查询点赞
	 * 
	 */
	@Override
	public Vote getVoteById(Long id) {
		return voteRepository.findOne(id);
	}
	/**
	 * 删除 点赞
	 */
	@Transactional
	@Override
	public void removeVote(Long id) {
		voteRepository.delete(id);
	}

}
