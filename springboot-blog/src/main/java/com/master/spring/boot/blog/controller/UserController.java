package com.master.spring.boot.blog.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.master.spring.boot.blog.domain.Authority;
import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.service.AuthorityService;
import com.master.spring.boot.blog.service.UserService;
import com.master.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.master.spring.boot.blog.vo.Response;

/**
 * 用户控制器
 * 
 * @author zhuky
 * @date 
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;

	/**
	 * 查询所有的用户
	 * 
	 * @param async
	 * @param pageIndex
	 * @param pageSize
	 * @param name
	 * @param model
	 * @return
	 */
	@GetMapping
	public ModelAndView list(@RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "name", required = false, defaultValue = "") String name, Model model) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<User> page = userService.findByNameLike(name, pageable);
		List<User> list = page.getContent();

		model.addAttribute("page", page);
		model.addAttribute("userList", list);

		return new ModelAndView(async == true ? "users/list :: #mainContainerReplace" : "users/list", "userModel", model);

	}
	
	/**
	 * 创建用户
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public ModelAndView createForm(Model model){
		model.addAttribute("user", new User(null,null,null,null,null));
		return new ModelAndView("/users/add","userModel",model);
	}
	/**
	 * 用户保存或更新
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Response> saveOrUpdate(User user, Long authorityId){
		
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.findById(authorityId));
		user.setAuthorities(authorities);
		
		try {
			userService.saveOrUpdate(user);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model){
		try {
			userService.deleteById(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false,e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return new ModelAndView("users/edit", "userModel", model);
	}
	
	
}
