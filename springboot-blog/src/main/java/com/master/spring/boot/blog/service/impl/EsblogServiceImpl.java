package com.master.spring.boot.blog.service.impl;


import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.master.spring.boot.blog.domain.User;
import com.master.spring.boot.blog.domain.es.Esblog;
import com.master.spring.boot.blog.repository.es.EsblogRepository;
import com.master.spring.boot.blog.service.EsblogService;
import com.master.spring.boot.blog.service.UserService;
import com.master.spring.boot.blog.vo.TagVO;



/**
 * EsblogService 实现
 * 
 * @author zhu
 *
 */
@Service
public class EsblogServiceImpl implements EsblogService {
	
	@Autowired
	private EsblogRepository esblogRepository;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private UserService userService;
	
	private static final String EMPTY_KEYWORD = "";
	private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
	
	
	@Override
	public void removeEsblog(String id) {
		esblogRepository.delete(id);
	}

	@Override
	public Esblog updateEsblog(Esblog esblog) {
		return esblogRepository.save(esblog);
	}

	@Override
	public Esblog getEsblogByBlogId(Long blogId) {
		return esblogRepository.findByBlogId(blogId);
	}

	@Override
	public Page<Esblog> listNewEsblogs(String keyword, Pageable pageable) {
		Sort sort = new Sort(Direction.DESC, "createTime");
		if(pageable.getSort() == null){
			pageable =new PageRequest(pageable.getPageNumber(),pageable.getPageSize(), sort);
		}
		return esblogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
	}

	@Override
	public Page<Esblog> listHotEsblogs(String keyword, Pageable pageable) {
		Sort sort = new Sort(Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
		if(pageable.getSort() == null){
			pageable =new PageRequest(pageable.getPageNumber(),pageable.getPageSize(), sort);
		}
		return esblogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);

	}

	@Override
	public Page<Esblog> listEsblogs(Pageable pageable) {
		return esblogRepository.findAll(pageable);
	}

	@Override
	public List<Esblog> listTop5NewEsblogs() {
		return this.listNewEsblogs(EMPTY_KEYWORD, TOP_5_PAGEABLE).getContent();
	}

	@Override
	public List<Esblog> listTop5HotEsblogs() {
		return this.listHotEsblogs(EMPTY_KEYWORD, TOP_5_PAGEABLE).getContent();
	}

	@Override
	public List<TagVO> listTop30Tags() {	

		List<TagVO> list = new ArrayList<>();
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("tags").field("tags").order(Terms.Order.count(false)).size(30))
				.build();
		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("tags"); 
	        
		Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
 
            list.add(new TagVO(actiontypeBucket.getKey().toString(),
                    actiontypeBucket.getDocCount()));
        }
		return list;
		
	}

	@Override
	public List<User> listTop12Users() {

		List<String> usernamelist = new ArrayList<>();
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(12))
				.build();
		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("users"); 
	        
        Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
            String username = actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }
        
        // 根据用户名，查出用户的详细信息
        List<User> list = userService.listUsersByUsernames(usernamelist);
        
		return list;
	}

}
