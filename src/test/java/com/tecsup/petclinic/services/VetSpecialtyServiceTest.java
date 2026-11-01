package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso 6 : Pruebas unitarias de la tabla vet_specialties
 */
@SpringBootTest
@Slf4j
public class VetSpecialtyServiceTest {

	@Autowired
	private VetSpecialtyService vetSpecialtyService;

	/**
	 * Asigna una especialidad a un veterinario
	 * (Henry Stevens, id 5 -> dentistry, id 3)
	 */
	@Test
	public void testAssignSpecialtyToVet() {

		Integer VET_ID = 5;
		Integer SPECIALTY_ID = 3;

		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(VET_ID)
				.specialtyId(SPECIALTY_ID)
				.certificationDate(LocalDate.of(2025, 5, 20))
				.yearsExperience(1)
				.primary(false)
				.notes("Basic dental course")
				.build();

		VetSpecialtyDTO assigned = null;
		try {
			assigned = this.vetSpecialtyService.assign(dto);
		} catch (VetSpecialtyAlreadyExistsException e) {
			fail(e.getMessage());
		}
		log.info("ASSIGNED : " + assigned);

		assertEquals(VET_ID, assigned.getVetId());
		assertEquals(SPECIALTY_ID, assigned.getSpecialtyId());

		List<VetSpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVet(VET_ID);
		assertTrue(specialties.stream().anyMatch(vs -> vs.getSpecialtyId().equals(SPECIALTY_ID)));
	}

	/**
	 * Asignar dos veces la misma especialidad debe fallar
	 * (Helen Leary, id 2 ya tiene radiology, id 1)
	 */
	@Test
	public void testAssignDuplicateSpecialty() {

		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(2)
				.specialtyId(1)
				.yearsExperience(3)
				.primary(false)
				.build();

		assertThrows(VetSpecialtyAlreadyExistsException.class, () -> this.vetSpecialtyService.assign(dto));
	}

	/**
	 * Lista las especialidades de un veterinario (Linda Douglas, id 3)
	 */
	@Test
	public void testFindSpecialtiesByVet() {

		Integer VET_ID = 3;
		int SIZE_EXPECTED = 2;

		List<VetSpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVet(VET_ID);

		assertEquals(SIZE_EXPECTED, specialties.size());
	}

	/**
	 * Un veterinario sin especialidades devuelve una lista vacia (James Carter, id 1)
	 */
	@Test
	public void testFindSpecialtiesByVetEmpty() {

		List<VetSpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVet(1);

		assertTrue(specialties.isEmpty());
	}

	/**
	 * Lista los veterinarios que tienen una especialidad (surgery, id 2)
	 */
	@Test
	public void testFindVetsBySpecialty() {

		Integer SPECIALTY_ID = 2;
		int SIZE_EXPECTED = 2;

		List<VetSpecialtyDTO> vets = this.vetSpecialtyService.findVetsBySpecialty(SPECIALTY_ID);

		assertEquals(SIZE_EXPECTED, vets.size());
		assertTrue(vets.stream().anyMatch(vs -> vs.getVetId().equals(3)));
		assertTrue(vets.stream().anyMatch(vs -> vs.getVetId().equals(4)));
	}

	/**
	 * Asigna una especialidad y luego actualiza los anios de experiencia
	 * y la marca como principal (Sharon Jenkins, id 6 -> radiology, id 1)
	 */
	@Test
	public void testUpdateVetSpecialty() {

		Integer VET_ID = 6;
		Integer SPECIALTY_ID = 1;
		Integer UP_YEARS = 4;

		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(VET_ID)
				.specialtyId(SPECIALTY_ID)
				.certificationDate(LocalDate.of(2022, 3, 10))
				.yearsExperience(2)
				.primary(false)
				.build();

		try {
			// ------------ Assign ---------------
			VetSpecialtyDTO assigned = this.vetSpecialtyService.assign(dto);
			log.info(">> " + assigned);

			// ------------ Update ---------------
			assigned.setYearsExperience(UP_YEARS);
			assigned.setPrimary(true);
			this.vetSpecialtyService.update(assigned);

			// ------------ Validate ---------------
			VetSpecialtyDTO found = this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID);
			log.info(">>>> " + found);

			assertEquals(UP_YEARS, found.getYearsExperience());
			assertTrue(found.getPrimary());

		} catch (VetSpecialtyAlreadyExistsException | VetSpecialtyNotFoundException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Asigna una especialidad, quita la relacion y comprueba que ya no existe
	 * (Rafael Ortega, id 4 -> dentistry, id 3)
	 */
	@Test
	public void testRemoveSpecialtyFromVet() {

		Integer VET_ID = 4;
		Integer SPECIALTY_ID = 3;

		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(VET_ID)
				.specialtyId(SPECIALTY_ID)
				.yearsExperience(1)
				.primary(false)
				.build();

		// ------------ Assign ---------------
		try {
			this.vetSpecialtyService.assign(dto);
		} catch (VetSpecialtyAlreadyExistsException e) {
			fail(e.getMessage());
		}

		// ------------ Remove ---------------
		try {
			this.vetSpecialtyService.remove(VET_ID, SPECIALTY_ID);
		} catch (VetSpecialtyNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		assertThrows(VetSpecialtyNotFoundException.class,
				() -> this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID));
	}

	/**
	 * Quitar una relacion que no existe debe lanzar excepcion
	 */
	@Test
	public void testRemoveSpecialtyNotAssigned() {

		assertThrows(VetSpecialtyNotFoundException.class, () -> this.vetSpecialtyService.remove(1, 2));
	}
}
