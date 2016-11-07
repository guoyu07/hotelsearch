package com.agoda.hotel.config;

import org.springframework.http.HttpStatus;

public class APIAccessStatus {

	public enum APIStatus {
		ACTIVE(HttpStatus.OK), SUSPENDED(HttpStatus.TOO_MANY_REQUESTS), INVALID_KEY(HttpStatus.BAD_REQUEST);

		private HttpStatus httpStatus;

		APIStatus(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;

		}

		public HttpStatus getHttpStatus() {
			return this.httpStatus;
		}
	}

	private boolean allowed;

	private APIStatus status;

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public APIStatus getStatus() {
		return status;
	}

	public void setStatus(APIStatus status) {
		this.status = status;
	}

}
