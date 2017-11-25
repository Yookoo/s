package com.master.spring.boot.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ä¸»é¡µæŽ§åˆ¶å™?.
 * 
 * @since 1.0.0 2017å¹?3æœ?8æ—?
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
	
	@GetMapping
	public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="tag",required=false) Long tag) {
		System.out.print("order:" +order + ";tag:" +tag );
		return "redirect:/index?order="+order+"&tag="+tag;
	}

}
