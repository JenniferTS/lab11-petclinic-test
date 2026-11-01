package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetTypeRepository
	extends JpaRepository<PetType, Integer> {

	// Buscar tipos por nombre
	List<PetType> findByName(String name);

	// Buscar tipos activos o inactivos
	List<PetType> findByActive(Boolean active);

	@Override
	List<PetType> findAll();

}
