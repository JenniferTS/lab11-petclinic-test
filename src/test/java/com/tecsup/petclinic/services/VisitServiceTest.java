package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso 4 : Pruebas unitarias de la tabla visits
 */
@SpringBootTest
@Slf4j
public class VisitServiceTest {

	@Autowired
	private VisitService visitService;

	/**
	 * Busca una visita que existe en data.sql
	 */
	@Test
	public void testFindVisitById() {

		Integer ID = 3;
		String DESCRIPTION_EXPECTED = "neutered";
		VisitDTO visit = null;

		try {
			visit = this.visitService.findById(ID);
		} catch (VisitNotFoundException e) {
			fail(e.getMessage());
		}
		log.info("VISIT FOUND : " + visit);

		assertEquals(DESCRIPTION_EXPECTED, visit.getDescription());
		assertEquals(8, visit.getPetId());
		assertEquals(0, new BigDecimal("250.00").compareTo(visit.getCost()));
	}

	/**
	 * Busca un id que no existe, debe lanzar VisitNotFoundException
	 */
	@Test
	public void testFindVisitByIdNotFound() {

		assertThrows(VisitNotFoundException.class, () -> this.visitService.findById(9999));
	}

	/**
	 * Busca las visitas de una mascota (Samantha, id 7)
	 */
	@Test
	public void testFindVisitsByPetId() {

		Integer PET_ID = 7;
		int SIZE_EXPECTED = 2;

		List<VisitDTO> visits = this.visitService.findByPetId(PET_ID);

		assertEquals(SIZE_EXPECTED, visits.size());
	}

	/**
	 * Busca las visitas atendidas por un veterinario (Helen Leary, id 2)
	 */
	@Test
	public void testFindVisitsByVetId() {

		Integer VET_ID = 2;
		int SIZE_EXPECTED = 2;

		List<VisitDTO> visits = this.visitService.findByVetId(VET_ID);

		assertEquals(SIZE_EXPECTED, visits.size());
		visits.forEach(visit -> assertEquals("rabies shot", visit.getDescription()));
	}

	/**
	 * Busca las visitas registradas en el anio 2024
	 */
	@Test
	public void testFindVisitsByDateBetween() {

		LocalDate START = LocalDate.of(2024, 1, 1);
		LocalDate END = LocalDate.of(2024, 12, 31);

		List<VisitDTO> visits = this.visitService.findByVisitDateBetween(START, END);

		assertFalse(visits.isEmpty());
		visits.forEach(visit -> assertEquals(2024, visit.getVisitDate().getYear()));
	}

	/**
	 * Registra una visita nueva
	 */
	@Test
	public void testCreateVisit() {

		Integer PET_ID = 1;
		Integer VET_ID = 5;
		LocalDate VISIT_DATE = LocalDate.of(2026, 10, 30);
		String DESCRIPTION = "vaccination";
		BigDecimal COST = new BigDecimal("80.50");

		VisitDTO visitDTO = VisitDTO.builder()
				.petId(PET_ID)
				.vetId(VET_ID)
				.visitDate(VISIT_DATE)
				.description(DESCRIPTION)
				.cost(COST)
				.build();

		VisitDTO newVisit = this.visitService.create(visitDTO);
		log.info("VISIT CREATED : " + newVisit);

		assertNotNull(newVisit.getId());
		assertEquals(PET_ID, newVisit.getPetId());
		assertEquals(VET_ID, newVisit.getVetId());
		assertEquals(VISIT_DATE, newVisit.getVisitDate());
		assertEquals(DESCRIPTION, newVisit.getDescription());
		assertEquals(0, COST.compareTo(newVisit.getCost()));
	}

	/**
	 * Registra una visita y actualiza descripcion y costo
	 */
	@Test
	public void testUpdateVisit() {

		VisitDTO visitDTO = VisitDTO.builder()
				.petId(1)
				.vetId(5)
				.visitDate(LocalDate.of(2026, 10, 15))
				.description("general checkup")
				.cost(new BigDecimal("60.00"))
				.build();

		String UP_DESCRIPTION = "checkup and deworming";
		BigDecimal UP_COST = new BigDecimal("95.00");

		// ------------ Create ---------------
		VisitDTO visitCreated = this.visitService.create(visitDTO);
		log.info(">> " + visitCreated);

		// ------------ Update ---------------
		visitCreated.setDescription(UP_DESCRIPTION);
		visitCreated.setCost(UP_COST);

		VisitDTO visitUpdated = this.visitService.update(visitCreated);
		log.info(">>>> " + visitUpdated);

		// ------------ Validate ---------------
		VisitDTO visitFound = null;
		try {
			visitFound = this.visitService.findById(visitCreated.getId());
		} catch (VisitNotFoundException e) {
			fail(e.getMessage());
		}

		assertEquals(UP_DESCRIPTION, visitFound.getDescription());
		assertEquals(0, UP_COST.compareTo(visitFound.getCost()));
	}

	/**
	 * Registra una visita, la elimina y comprueba que ya no existe
	 */
	@Test
	public void testDeleteVisit() {

		VisitDTO visitDTO = VisitDTO.builder()
				.petId(1)
				.vetId(5)
				.visitDate(LocalDate.of(2026, 9, 1))
				.description("nail trimming")
				.cost(new BigDecimal("25.00"))
				.build();

		// ------------ Create ---------------
		VisitDTO visitCreated = this.visitService.create(visitDTO);
		log.info("" + visitCreated);

		// ------------ Delete ---------------
		try {
			this.visitService.delete(visitCreated.getId());
		} catch (VisitNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		assertThrows(VisitNotFoundException.class, () -> this.visitService.findById(visitCreated.getId()));
	}

	/**
	 * Eliminar una visita que no existe debe lanzar excepcion
	 */
	@Test
	public void testDeleteVisitNotFound() {

		assertThrows(VisitNotFoundException.class, () -> this.visitService.delete(9999));
	}
}
