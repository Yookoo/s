package com.master.spring.boot.blog.vo;
/**
 * 统一的返回对象
 * 
 * @author ZKY
 *
 */
public class Response {

	
	private boolean success;
	
	private String message;
	
	private Object body;

	protected Response() {
		super();
	}

	public Response(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public Response(boolean success, String message, Object body) {
		super();
		this.success = success;
		this.message = message;
		this.body = body;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	
}
