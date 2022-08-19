package com.isacore.quality.exception;

@SuppressWarnings("serial")
public abstract class QualityException extends RuntimeException {

	@Override
	public abstract String getMessage();
}
