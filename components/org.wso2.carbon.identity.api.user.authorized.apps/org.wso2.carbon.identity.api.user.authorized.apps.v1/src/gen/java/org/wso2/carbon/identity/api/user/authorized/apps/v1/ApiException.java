package org.wso2.carbon.identity.api.user.authorized.apps.v1;
//comment
public class ApiException extends Exception{
	private int code;
	public ApiException (int code, String msg) {
		super(msg);
		this.code = code;
	}
}
