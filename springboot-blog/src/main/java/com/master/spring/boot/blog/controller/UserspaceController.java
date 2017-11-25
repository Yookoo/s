package com.master.spring.boot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.master.spring.boot.blog.domain.Blog;
import com.master.spring.boot.blog.domain.Catalog;
import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.domain.Vote;
import com.master.spring.boot.blog.domain.es.Esblog;
import com.master.spring.boot.blog.service.BlogService;
import com.master.spring.boot.blog.service.CatalogService;
import com.master.spring.boot.blog.service.EsblogService;
import com.master.spring.boot.blog.service.UserService;
import com.master.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.master.spring.boot.blog.vo.Response;

/**
 * 用户主页空间控制器.
 * 
 * @since 1.0.0 2017年8月24日 09:40:45
 * @author zhu 
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Value("${file.server.url}")
	private String fileServerUrl;
	
	@Autowired
	private EsblogService esblogService;
	
	@Autowired
	private BlogService blogService;

	@Autowired
	private CatalogService catalogService;
	/**
	 * 用户的个人主页
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username , Model model){
		User user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user" , user);
		return "redirect:/u/"+ username +"/blogs" ;
	}
	
	/**
	 * 用户的博客列表
	 * @param username
	 * @param order
	 * @param category
	 * @param keyword
	 * @return
	 */
	
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="category",required=false ) Long catalogId,
			@RequestParam(value="keyword",required=false , defaultValue="") String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false , defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false , defaultValue="10") int pageSize,
			Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		
		Page<Blog> page = null;
				
		if (catalogId!= null && catalogId >0) {//分类查询
			Catalog catalog = catalogService.getCatalogById(catalogId);
			Sort sort = new Sort(Direction.DESC,"createTime");
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByCatalog(catalog, pageable);
		}else if("hot".equals(order)){//最热查询
			Sort sort = new Sort(Direction.DESC,"readSize", "commentSize", "voteSize");
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
		} else if("new".equals(order)){//最新查询
			Sort sort = new Sort(Direction.DESC,"createTime");
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
		}   
		
		List<Blog> list = page.getContent();
		System.out.println(list.toArray());
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		return (async == true ? "/userspace/u :: #mainContainerReplace":"/userspace/u");
	}
	
	
	/**
	 * 获取博客并在页面中展示
	 * @param id
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public String listBlogsByOrder(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		User principal = null;  //委托人，当前登录的用户；
		
		Blog blog = null;
		Esblog esblog = esblogService.getEsblogByBlogId(id);

		
		if(esblog == null){
			blog = blogService.getBlogById(id);
		}else {
			blog = new Blog(esblog);
}
		blogService.readingIncrease(id);//将博客的阅读量+1
		
		//判断当前用户是否是博客的主人
		boolean isBlogOnwer = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//获取身份验证信息
		if(authentication != null && authentication.isAuthenticated()){
			principal = (User)authentication.getPrincipal();
			if(principal != null && username.equals(principal.getUsername())){
				isBlogOnwer = true;
			}
		}
		//判断用户点赞情况
		Vote currentVote = null;
		List<Vote> votes = blog.getVotes();
		if(principal != null){
			for (Vote vote : votes) {
				if(principal.getUsername().equals(vote.getUser().getUsername())){
					currentVote =vote;
				};
				break;
			}
		}

		model.addAttribute("currentVote",currentVote);
		model.addAttribute("isBlogOwner", isBlogOnwer);
		model.addAttribute("blogModel", blog);
		return "/userspace/blog";
	}
	
	//-------------------------------------------------博客管理-------------------------------------------------//

	/**
	 * 新增博客
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") //权限判断
	public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", new Blog(null,null,null));
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("fileServerUrl", fileServerUrl);
		return new ModelAndView("/userspace/blogedit","blogModel", model);
	}
	
	/**
	 * 编辑博客
	 * @param username
	 * @param id
	 * @param model
	 * @return
	 */

	@GetMapping("/{username}/blogs/edit/{id}")
	@PreAuthorize("authentication.name.equals(#username)") //权限判断
	public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);

		model.addAttribute("catalogs", catalogs);	
		model.addAttribute("blog", blogService.getBlogById(id));
		model.addAttribute("fileServerUrl", fileServerUrl);
		return new ModelAndView("userspace/blogedit","blogModel", model);
	}
	/**
	 * 保存博客
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") //权限验证
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog){
		//分类必填
		if(blog.getCatalog().getId() == null){
			return ResponseEntity.ok().body(new Response(false, "未选择分类"));
		}
		
		try {
			if(blog.getId() != null){
				//修改博客
				Blog orignalBlog = blogService.getBlogById(blog.getId());
				orignalBlog.setTitle(blog.getTitle());
				orignalBlog.setSummary(blog.getSummary());
				orignalBlog.setContent(blog.getContent());
				orignalBlog.setCatalog(blog.getCatalog());
				orignalBlog.setTags(blog.getTags());
				blogService.saveBlog(orignalBlog);
			} else {
				//新增博客
				User user = (User)userDetailsService.loadUserByUsername(username);
				blog.setUser(user);
				blogService.saveBlog(blog);
			}
		} catch (ConstraintViolationException e) {
			//Bean 验证异常
			e.printStackTrace();
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}
		
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "处理成功",redirectUrl)); 
	}
	
	/**
	 * 删除博客
	 * @param username
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)") //权限验证
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id){
		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
	
	//-------------------------------------------------个人信息管理-------------------------------------------------//
	
	/**
	 * 获取个人设置页面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") //权限判断
	public ModelAndView profile(@PathVariable("username") String username, Model model){
		User user = (User) userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("fileServerUrl", fileServerUrl);
		return new ModelAndView("/userspace/profile","userModel",model);	
	}
	
	/**
	 * 保存个人设置
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") //权限判断
	public String profile(@PathVariable("username") String username, User user){
		User originalUser = userService.findById(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setName(user.getName());
		
		//判断密码是否变更
		String rawPassword =originalUser.getPassword();
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		//为新密码加密
		String encodedPassword = encoder.encode(user.getPassword());
		//判断两个密码是否相等（密码都是加密过的）
		boolean isMatch = encoder.matches(rawPassword, encodedPassword);
		
		if(!isMatch){
			originalUser.setPassword(encodedPassword);
		}
		userService.saveOrUpdate(originalUser);
		return "redirect:/u/"+ username +"/profile";	
	}
	
	/**
	 * 获取编辑头像的界面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") //权限判断
	public ModelAndView avatar(@PathVariable("username") String username, Model model){
		User user = (User) userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/avatar", "userModel", model);	
	}
	 
	/**
	 * 保存头像
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") //权限判断
	public ResponseEntity<Response> avatar(@PathVariable("username") String username,@RequestBody User user ){
		String avatarUrl = user.getAvatar();
		User originalUser = userService.findById(user.getId());
		originalUser.setAvatar(avatarUrl);
		userService.saveOrUpdate(originalUser);
		return ResponseEntity.ok().body(new Response(true,"处理成功", avatarUrl));
	}
 
}
