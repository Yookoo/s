package com.master.spring.boot.blog.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.Blog;
import com.master.spring.boot.blog.domain.Catalog;
import com.master.spring.boot.blog.domain.Comment;
import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.domain.Vote;
import com.master.spring.boot.blog.domain.es.Esblog;
import com.master.spring.boot.blog.repository.BlogRepository;
import com.master.spring.boot.blog.service.BlogService;
import com.master.spring.boot.blog.service.EsblogService;

/**
 * Blog 服务.
 * 
 * @since 1.0.0 2017年9月7日
 * @author zhu
 */
@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;
 
	@Autowired
	private EsblogService esblogService;
	
	/**
	 * 保存博客
	 */
	@Transactional
	@Override
	public Blog saveBlog(Blog blog) {
		 
		Long blogId = blog.getId(); 
		
		Esblog esblog = null ;
		
		Blog returnBlog = blogRepository.save(blog);
		if(blogId == null){
			//新建
			esblog = new Esblog(returnBlog);
		} else {
			//更新
			esblog = esblogService.getEsblogByBlogId(blogId);
			esblog.update(returnBlog);
		}
		
		esblogService.updateEsblog(esblog);
		return returnBlog;
	}

	@Transactional
	@Override
	public void removeBlog(Long id) {
		blogRepository.delete(id);
		Esblog esblog = esblogService.getEsblogByBlogId(id);
		esblogService.removeEsblog(esblog.getId());
	}

	@Override
	public Blog getBlogById(Long id) {
		return blogRepository.findOne(id);
	}

	@Override
	public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		String tags = title;
		Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user, tags,user, pageable);
		return blogs;
	}

	@Override
	public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
		return blogs;
	}
	/**
	 * 
	 * 阅读量递增
	 */
	@Override
	public void readingIncrease(Long id) {
		Blog blog = blogRepository.findOne(id);
		blog.setReadSize(blog.getReadSize()+1); // 在原有的阅读量基础上递增1
		this.saveBlog(blog);
	}
	/**
	 * 创建评论
	 * 
	 */
	@Transactional
	@Override
	public Blog createComment(Long blogId, String commentContent) {
		Blog originalBlog = blogRepository.findOne(blogId);
		User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Comment comment = new Comment(commentContent, user);
		originalBlog.addComment(comment);	
		return this.saveBlog(originalBlog);
	}

	/**
	 * 删除评论
	 */
	@Transactional
	@Override
	public void removeComment(Long blogId, Long commentId) {
		Blog originalBlog = blogRepository.findOne(blogId);
		originalBlog.removeComment(commentId);
		this.saveBlog(originalBlog);
	}
	/**
	 * 点赞
	 */
	@Transactional
	@Override
	public Blog createVote(Long blogId) {
		Blog originalBlog = blogRepository.findOne(blogId);
		User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Vote vote = new Vote(user);
		boolean isExist = originalBlog.addVote(vote);
		if(isExist){
			throw new IllegalArgumentException("您已经点过赞了！");
		}
		return this.saveBlog(originalBlog);
	}
	/**
	 * 取消点赞
	 */
	@Transactional
	@Override
	public void removeVote(Long blogId, Long voteId) {
		Blog originalBlog = blogRepository.findOne(blogId);
		originalBlog.removeVote(voteId);
		this.saveBlog(originalBlog);
	}

	/**
	 * 根据分类 查询 博客
	 */
	@Override
	public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
		return blogRepository.findByCatalog(catalog, pageable);
	}
}
