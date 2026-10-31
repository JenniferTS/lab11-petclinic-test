package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository
	extends JpaRepository<Owner, Integer> {

	// Buscar duenos por apellido
	List<Owner> findByLastName(String lastName);

	// Buscar duenos por ciudad
	List<Owner> findByCity(String city);

	@Override
	List<Owner> findAll();

}
