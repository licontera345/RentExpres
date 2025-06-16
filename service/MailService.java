package com.pinguela.rentexpres.service;

public interface MailService {

	public boolean enviar(String email, String asunto, String mensaje);

}
