
package com.master.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.master.spring.boot.blog.domain.Authority;

/**
 * AuthorityRepository
 * 
 * @author ZKY
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
