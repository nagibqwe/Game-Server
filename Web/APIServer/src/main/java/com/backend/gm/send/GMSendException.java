package com.backend.gm.send;

public class GMSendException extends Exception {

	private static final long serialVersionUID = 3370221588828188995L;

	private GMSendErrorType resultType;

	public GMSendException(GMSendErrorType resultType, Throwable cause) {
		super(cause);
		this.resultType = resultType;
	}

	GMSendException(GMSendErrorType resultType, String message, Throwable cause) {
		super(message, cause);
		this.resultType = resultType;
	}

	public GMSendErrorType getResultType() {
		return resultType;
	}

	public void setResultType(GMSendErrorType resultType) {
		this.resultType = resultType;
	}

}
