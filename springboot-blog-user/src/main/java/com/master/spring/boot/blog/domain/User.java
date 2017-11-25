package com.master.spring.boot.blog.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User 实体
 * @author ZKY
 */
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //自增策略
	private Long id;
	
	@NotEmpty(message = "姓名不能为空")
	@Size(min = 2, max = 20)
	@Column(nullable = false, length = 20)
	private String name;
	
	@NotEmpty(message = "邮箱不能为空")
	@Size(max = 50)
	@Email(message = "邮箱格式不正确")
	@Column(nullable = false, length = 50)
	private String email;
	
	@NotEmpty(message = "帐号不能为空")
	@Size(min = 3, max = 20)
	@Column(nullable = false, length = 20 ,unique = true)
	private String username;
	
	@NotEmpty(message = "密码不能为空")
	@Size(max = 100)
	@Column(nullable = false, length = 100 ,unique = true)
	private String password;
	
	@Size(max = 100)
	@Column(length = 100)
	private String authorities;
	
	@Column(length = 200)
	private String avatar; //头像信息
	
	protected User() {
		super();
	}



	public User(Long id, String name, String email, String username, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + ", authorities="
				+ authorities + ", avatar=" + avatar + "]";
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}



	public String getAuthorities() {
		return authorities;
	}



	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
	
}
