package com.master.spring.boot.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.master.spring.boot.blog.vo.Menu;

/**
 * 后台管理的控制器
 * 
 * @author ZHU
 * @date 2017年8月18日 08:46:24
 */
@Controller
@RequestMapping("/admins")
public class AdminController {
 

	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listUsers(Model model) {
		List<Menu> list = new ArrayList<Menu>();
		list.add(new Menu("用户管理","/users"));
		model.addAttribute("list", list);
		return new ModelAndView("admins/index", "model", model);
	}
 
	 
}
