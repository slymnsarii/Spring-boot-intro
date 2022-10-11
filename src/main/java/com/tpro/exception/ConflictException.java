package com.tpro.exception;

public class ConflictException extends RuntimeException{//mevcut bir exception'a extends ediyoruz
	public ConflictException(String message) {//conflict:iki tane dosyanin field'in ayni ismi tasimasi, cakisma, karisiklik
		super(message); //super; parent'ina gider(RunTimeException), runtime constructoruna gidecek
						//super'deki benim yazdigim mesaj RunTimeException'a gidecek
	}
}
