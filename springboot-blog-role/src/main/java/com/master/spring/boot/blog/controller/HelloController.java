package com.master.spring.boot.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World æŽ§åˆ¶å™?
 * @author <a href="https://waylau.com">Way Lau</a> 
 * @date 2017å¹?1æœ?26æ—?
 */
@RestController
public class HelloController {

	@RequestMapping("/hello")
	public String hello() {
	    return "Hello World! Welcome to visit waylau.com!";
	}
}
