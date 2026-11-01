package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

	VetSpecialtyDTO assign(VetSpecialtyDTO vetSpecialtyDTO) throws VetSpecialtyAlreadyExistsException;

	VetSpecialtyDTO update(VetSpecialtyDTO vetSpecialtyDTO) throws VetSpecialtyNotFoundException;

	void remove(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

	VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

	List<VetSpecialtyDTO> findSpecialtiesByVet(Integer vetId);

	List<VetSpecialtyDTO> findVetsBySpecialty(Integer specialtyId);
}
