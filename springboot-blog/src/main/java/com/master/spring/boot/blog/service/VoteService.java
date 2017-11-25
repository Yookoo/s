package com.master.spring.boot.blog.service;

import com.master.spring.boot.blog.domain.Vote;

/**
 * 
 * 点赞 服务 接口
 * 
 * @author zhu
 *
 */
public interface VoteService {

	/**
	 * 根据id获取vote
	 * @param id
	 */
	Vote getVoteById(Long id);
	
	/**
	 * 删除 vote
	 * @param id
	 */
	void removeVote(Long id); 
}
