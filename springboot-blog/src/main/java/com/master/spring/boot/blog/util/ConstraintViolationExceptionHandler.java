package com.master.spring.boot.blog.util;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;



/**
 * Bean校验异常处理器
 * 
 * @author ZKY
 *
 */
public class ConstraintViolationExceptionHandler {

	

	public static String getMessage(final ConstraintViolationException e) {
		List<String> msglist = new ArrayList<String>();
		for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
			msglist.add(constraintViolation.getMessage());
		}
		String messages = StringUtils.join(msglist.toArray(),";");
		return messages;		
	}
}
