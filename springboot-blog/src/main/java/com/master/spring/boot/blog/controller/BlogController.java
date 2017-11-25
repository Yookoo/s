package com.master.spring.boot.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.domain.es.Esblog;
import com.master.spring.boot.blog.service.EsblogService;
import com.master.spring.boot.blog.vo.TagVO;

/**
 * Blog 控制器.
 * 
 * @since 1.0.0 2017年5月28日
 * @author zhu
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
	
	@Autowired
    private EsblogService esblogService;
	 
    @GetMapping
    public String listEsblogs(
            @RequestParam(value="order",required=false,defaultValue="new") String order,
            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {

        Page<Esblog> page = null;
        List<Esblog> list = null;
        boolean isEmpty = true; // 系统初始化时，没有博客数据
        try {
            if ("hot".equals(order)) { // 最热查询
                Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize","createTime"); 
                Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
                page = esblogService.listHotEsblogs(keyword, pageable);
            } else if ("new".equals(order)) { // 最新查询
                Sort sort = new Sort(Direction.DESC,"createTime"); 
                Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
                page = esblogService.listNewEsblogs(keyword, pageable);
            }

            isEmpty = false;
        } catch (Exception e) {
        	e.printStackTrace();
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = esblogService.listEsblogs(pageable);
        }  

        list = page.getContent();   // 当前所在页面数据列表


        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);

        // 首次访问页面才加载
        if (!async && !isEmpty) {
            List<Esblog> newest = esblogService.listTop5NewEsblogs();
            model.addAttribute("newest", newest);
            List<Esblog> hotest = esblogService.listTop5HotEsblogs();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esblogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esblogService.listTop12Users();
                    model.addAttribute("users", users);
                }

                return (async==true?"/index :: #mainContainerReplace":"/index");
            }

}
