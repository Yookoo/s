package com.master.spring.boot.blog.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.Comment;
import com.master.spring.boot.blog.repository.CommentRepository;
import com.master.spring.boot.blog.service.CommentService;

/**
 * 
 * 评论 业务 实现类
 * 
 * @author zhu
 *
 */

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	/**
	 * 查询评论
	 * 
	 */
	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.findOne(id);
	}
	/**
	 * 删除评论
	 */
	@Transactional
	@Override
	public void removeComment(Long id) {
		commentRepository.delete(id);
	}

}
