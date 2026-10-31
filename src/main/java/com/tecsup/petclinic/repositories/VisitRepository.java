package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository
	extends JpaRepository<Visit, Integer> {

	// Visitas de una mascota
	List<Visit> findByPetId(Integer petId);

	// Visitas atendidas por un veterinario
	List<Visit> findByVetId(Integer vetId);

	// Visitas entre dos fechas
	List<Visit> findByVisitDateBetween(LocalDate start, LocalDate end);

	@Override
	List<Visit> findAll();

}
