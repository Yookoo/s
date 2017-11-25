package com.master.spring.boot.blog.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.master.spring.boot.blog.domain.es.Esblog;

/**
 * 
 * Esblog Repository 接口
 * 
 * @author zhu
 *
 */

public interface EsblogRepository extends ElasticsearchCrudRepository<Esblog, String>{

	/**
	 * 模糊查询（去重）
	 * @param title
	 * @param summary
	 * @param content
	 * @param tags
	 * @param pageable
	 * @return
	 */
	Page<Esblog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,String summary, String content, String tags, Pageable pageable);

	/**
	 * 根据博客id查询EsBlog
	 * @param blogId
	 * @return
	 */
	Esblog findByBlogId(Long blogId);
}
