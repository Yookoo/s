package com.master.spring.boot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置类
 * 
 * @author ZKY
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用方法级别的安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();	
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(){
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);	//bcrypt 密码加密
		return authenticationProvider;
	}
	
	/**
	 * 自定义配置 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll()// 静态资源都可以访问
			.antMatchers("h2-console/**").permitAll() // h2控制台都可以访问
			.antMatchers("/admins/**").hasRole("ADMIN") //admins 需要管理员才可以访问
			.and().formLogin() //基于Form表单的登陆验证
			.loginPage("/login").failureUrl("/login-error") //定义登录页面
			.and().rememberMe().key("MASTER") //启用remember me
			.and().exceptionHandling().accessDeniedPage("/403"); //出现异常重定向到403错误页
		http.csrf().ignoringAntMatchers("h2-console/**"); //禁用H2控制台的CSRF防护
		http.headers().frameOptions().sameOrigin(); //允许同一来源的H2控制台的请求
	}
	/**
	 * 
	 * 
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(this.authenticationProvider());

	}
}
