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
import com.master.spring.boot.blog.domain.Vote;
import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.service.BlogService;
import com.master.spring.boot.blog.service.VoteService;
import com.master.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.master.spring.boot.blog.vo.Response;

/**
 * 
 * vote 控制器
 * 
 * @author zhu
 *
 */
@Controller
@RequestMapping("/votes")
public class VoteController {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private VoteService voteService;

	/**
	 * 创建点赞
	 * @param blogId
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
	public ResponseEntity<Response> createVote(Long blogId, String voteContent) {
 
		try {
			blogService.createVote(blogId);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
	}
	
	/**
	 * 取消点赞
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Long blogId) {
		
		boolean isVoteOwner = false;
		User user = voteService.getVoteById(id).getUser();
		
		// 判断操作用户是否是评论的所有者
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//获取身份验证信息
		if ( authentication !=null && authentication.isAuthenticated() &&  !authentication.getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) authentication.getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isVoteOwner = true;
			} 
		} 
		
		if (!isVoteOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
		}
		
		try {
			blogService.removeVote(blogId, id);
			voteService.removeVote(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}	
		return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
	}
}

