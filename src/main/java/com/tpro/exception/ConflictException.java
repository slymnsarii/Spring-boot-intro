package com.tpro.exception;

public class ConflictException extends RuntimeException{
	public ConflictException(String message) { //conflict:cakisma, karisiklik,
		super(message); //super parent'ina gider, runtime constructoruna gidecek
	}
}
