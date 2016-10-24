package com.ddy.message.exception;

/**
 * 业务异常
 * 
 * @author licf
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;
	private String errorCode;
	private String extraMsg;

	public BusinessException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public BusinessException(String message) {
		super(message);
		this.message = message;
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	/**
	 * @param message
	 * @param errorCode
	 */
	public BusinessException(String message, String errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getExtraMsg() {
		return extraMsg;
	}

	public void setExtraMsg(String extraMsg) {
		this.extraMsg = extraMsg;
	}

}
