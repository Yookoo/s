package com.master.spring.boot.blog.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.master.spring.boot.blog.domain.Comment;

/**
 * 评论 资源库 接口
 * 
 * @author zhu
 */
public interface CommentRepository extends JpaRepository<Comment,Long>{

}
