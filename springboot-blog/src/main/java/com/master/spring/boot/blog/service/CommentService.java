package com.master.spring.boot.blog.service;

import com.master.spring.boot.blog.domain.Comment;

/**
 * 
 * 评论 服务 接口
 * 
 * @author zhu
 *
 */
public interface CommentService {

	/**
	 * 根据id获取Comment
	 * @param id
	 */
	Comment getCommentById(Long id);
	
	/**
	 * 删除 评论
	 * @param id
	 */
	void removeComment(Long id); 
}
