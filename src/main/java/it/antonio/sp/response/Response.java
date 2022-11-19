package it.antonio.sp.response;

public class Response {
	public Boolean ok;
	public String errorMessage;
	public String successMessage;
	
	Response(Boolean ok, String errorMessage, String successMessage) {
		this.ok = ok;
		this.errorMessage = errorMessage;
		this.successMessage = successMessage;
	}
	
	public static Response Error() {
		return new Response(false, "", "");
	}
	
	public static Response Error(String errorMessage) {
		return new Response(false, errorMessage, "");
	}
	
	public static Response Success() {
		return new Response(true, "", "");
	}
	
	public static Response Success(String successMessage) {
		return new Response(true, "", successMessage);
	}
}
