package com.waylau.spring.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.waylau.spring.boot.domain.User;
import com.waylau.spring.boot.repository.UserRepository;

/**
 * User 控制器
 * @author ZKY
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public ModelAndView list(Model model){
		model.addAttribute("userList", userRepository.findAll());
		model.addAttribute("title","用户列表");
		return new ModelAndView("/users/list","userModel",model);
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Long id,Model model){
		model.addAttribute("user", userRepository.findOne(id));
		model.addAttribute("title","用户信息");
		return new ModelAndView("/users/view","userModel",model);
	}
	
	@GetMapping("/form")
	public ModelAndView createForm(Model model){
		model.addAttribute("user", new User(null,null,null));
		model.addAttribute("title","编辑用户");
		return new ModelAndView("/users/form","userModel",model);
	}
	
	@PostMapping
	public ModelAndView edit(User user){
		userRepository.save(user);
		return new ModelAndView("redirect:/users");
	}
	@GetMapping("del/{id}")
	public ModelAndView delete(@PathVariable("id") Long id){
		userRepository.delete(id);;
		return new ModelAndView("redirect:/users");
	}
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@GetMapping(value = "mod/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		User user = userRepository.findOne(id);
		model.addAttribute("user", user);
		model.addAttribute("title", "修改用户");
		return new ModelAndView("users/form", "userModel", model);
	}
}
