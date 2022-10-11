package com.tpro.exception;

public class ResourceNotFoundException extends RuntimeException{
	public ResourceNotFoundException(String message) {
		super(message);
		//eger buraya this dersek bu method'u cagiran obje'yi refer eder,
		//ama super derseniz icinde bulundugunuz class'in yani ResourceNotFoundException class'inin
		//parent'ini(RuntimeException) refer edersiniz
	}

}
