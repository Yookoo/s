package com.master.spring.boot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.master.spring.boot.blog.domain.Blog;
import com.master.spring.boot.blog.domain.Comment;
import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.service.BlogService;
import com.master.spring.boot.blog.service.CommentService;
import com.master.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.master.spring.boot.blog.vo.Response;

/**
 * 
 * 评论 控制器
 * 
 * @author zhu
 *
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;
	/**
	 * 获取评论列表
	 * @param blogId
	 * @param model
	 * @return
	 */
	@GetMapping
	public String listComments(@RequestParam(value="blogId", required=true) Long blogId, Model model) {
		
		Blog blog = blogService.getBlogById(blogId);
		
		List<Comment> comments = blog.getComments();
		

		//判断当前用户是否是评论的主人
		String commentOnwer = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//获取身份验证信息
		if(authentication != null && authentication.isAuthenticated()){
			User principal = (User)authentication.getPrincipal();
			if(principal != null){
				commentOnwer = principal.getUsername();
			}
		}
		model.addAttribute("commentOwner", commentOnwer);
		model.addAttribute("comments", comments);
		return "/userspace/blog :: #mainContainerReplace";
	}
	/**
	 * 发表评论
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
	public ResponseEntity<Response> createComment(Long blogId, String commentContent) {
 
		try {
			blogService.createComment(blogId, commentContent);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
	}
	
	/**
	 * 删除评论
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Long blogId) {
		
		boolean isCommentOwner = false;
		User user = commentService.getCommentById(id).getUser();
		
		// 判断操作用户是否是评论的所有者
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//获取身份验证信息
		if ( authentication !=null && authentication.isAuthenticated() &&  !authentication.getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) authentication.getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isCommentOwner = true;
			} 
		} 
		
		if (!isCommentOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
		}
		
		try {
			blogService.removeComment(blogId, id);
			commentService.removeComment(id);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}	
		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
	}
}

