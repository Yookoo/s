package com.master.spring.boot.blog.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.master.spring.boot.blog.domain.Comment;
import com.master.spring.boot.blog.domain.Vote;

/**
 * Vote 资源库 接口
 * 
 * @author zhu
 */
public interface VoteRepository extends JpaRepository<Vote,Long>{

}
