package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando no existe la especialidad buscada
 */
public class SpecialtyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public SpecialtyNotFoundException(String message) {
		super(message);
	}

}
