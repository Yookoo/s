package com.waylau.spring.boot.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Hello World 控制器
 * @author <a href="https://waylau.com">Way Lau</a> 
 * @date 2017年1月26日
 */
@RestController
public class HelloController {

	@RequestMapping("/hello")
	public ModelAndView hello(Model model) {
		model.addAttribute("title", "helloworld!");
	    return  new ModelAndView("/helloworld","userModel",model );
	}
}
