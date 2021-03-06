package com.master.spring.boot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.master.spring.boot.blog.domain.Blog;
import com.master.spring.boot.blog.domain.Catalog;
import com.master.spring.boot.blog.domain.Comment;
import com.master.spring.boot.blog.domain.User;

/**
 * Blog 服务接口.
 * 
 * @since 1.0.0 2017年4月7日
 * @author zhu
 */
public interface BlogService {
	/**
	 * 保存Blog
	 * @param EsBlog
	 * @return
	 */
	Blog saveBlog(Blog blog);
	
	/**
	 * 删除Blog
	 * @param id
	 * @return
	 */
	void removeBlog(Long id);
	
	/**
	 * 根据id获取Blog
	 * @param id
	 * @return
	 */
	Blog getBlogById(Long id);
 
	/**
	 * 根据用户进行博客名称分页模糊查询（最新）
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable);
 
	/**
	 * 根据用户进行博客名称分页模糊查询（最热）
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable);

	/**
	 * 阅读量递增
	 * @param id
	 */
	void readingIncrease(Long id);
	
	/**
	 * 创建评论
	 * @param blogId
	 * @param commentContent
	 */
	Blog createComment(Long blogId, String commentContent);
	
	/**
	 * 删除 评论
	 * @param blogId
	 * @param commentId
	 */
	void removeComment(Long blogId, Long commentId); 
	
	/**
	 * 点赞
	 * @param blogId
	 */
	Blog createVote(Long blogId);
	
	/**
	 * 取消点赞
	 * @param blogId
	 * @param commentId
	 */
	void removeVote(Long blogId, Long voteId); 
 
	
	/**
	 * 根据分类查询博客
	 * @param catalog
	 * @param pageable
	 * @return 
	 */
	Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);
	 
}
