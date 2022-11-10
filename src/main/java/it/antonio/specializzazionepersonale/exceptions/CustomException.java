package it.antonio.specializzazionepersonale.exceptions;

public class CustomException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException(String errorMessage, Throwable err) {
	    super(errorMessage, err);
	}
}
