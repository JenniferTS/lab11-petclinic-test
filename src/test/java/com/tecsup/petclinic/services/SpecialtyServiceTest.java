package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso 3 : Pruebas unitarias de la tabla specialties
 */
@SpringBootTest
@Slf4j
public class SpecialtyServiceTest {

	@Autowired
	private SpecialtyService specialtyService;

	/**
	 * Busca una especialidad que existe en data.sql
	 */
	@Test
	public void testFindSpecialtyById() {

		Integer ID = 2;
		String NAME_EXPECTED = "surgery";
		String OFFICE_EXPECTED = "Maryland";
		SpecialtyDTO specialty = null;

		try {
			specialty = this.specialtyService.findById(ID);
		} catch (SpecialtyNotFoundException e) {
			fail(e.getMessage());
		}
		log.info("SPECIALTY FOUND : " + specialty);

		assertEquals(NAME_EXPECTED, specialty.getName());
		assertEquals(OFFICE_EXPECTED, specialty.getOffice());
	}

	/**
	 * Busca un id que no existe, debe lanzar SpecialtyNotFoundException
	 */
	@Test
	public void testFindSpecialtyByIdNotFound() {

		assertThrows(SpecialtyNotFoundException.class, () -> this.specialtyService.findById(9999));
	}

	/**
	 * Busca especialidades por nombre
	 */
	@Test
	public void testFindSpecialtyByName() {

		String NAME = "dentistry";
		int SIZE_EXPECTED = 1;

		List<SpecialtyDTO> specialties = this.specialtyService.findByName(NAME);

		assertEquals(SIZE_EXPECTED, specialties.size());
		assertEquals("Terranova", specialties.get(0).getOffice());
	}

	/**
	 * Busca especialidades por consultorio
	 */
	@Test
	public void testFindSpecialtyByOffice() {

		String OFFICE = "Farewell";

		List<SpecialtyDTO> specialties = this.specialtyService.findByOffice(OFFICE);

		assertFalse(specialties.isEmpty());
		assertEquals("radiology", specialties.get(0).getName());
	}

	/**
	 * Crea una especialidad nueva
	 */
	@Test
	public void testCreateSpecialty() {

		String NAME = "cardiology";
		String OFFICE = "Lima";
		Integer OPEN_HOUR = 8;
		Integer CLOSE_HOUR = 16;

		SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
				.name(NAME)
				.office(OFFICE)
				.openHour(OPEN_HOUR)
				.closeHour(CLOSE_HOUR)
				.build();

		SpecialtyDTO newSpecialty = this.specialtyService.create(specialtyDTO);
		log.info("SPECIALTY CREATED : " + newSpecialty);

		assertNotNull(newSpecialty.getId());
		assertEquals(NAME, newSpecialty.getName());
		assertEquals(OFFICE, newSpecialty.getOffice());
		assertEquals(OPEN_HOUR, newSpecialty.getOpenHour());
		assertEquals(CLOSE_HOUR, newSpecialty.getCloseHour());
	}

	/**
	 * Crea una especialidad y actualiza su consultorio y horario
	 */
	@Test
	public void testUpdateSpecialty() {

		SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
				.name("dermatology")
				.office("Arequipa")
				.openHour(9)
				.closeHour(13)
				.build();

		String UP_OFFICE = "Yanahuara";
		Integer UP_OPEN_HOUR = 14;
		Integer UP_CLOSE_HOUR = 20;

		// ------------ Create ---------------
		SpecialtyDTO specialtyCreated = this.specialtyService.create(specialtyDTO);
		log.info(">> " + specialtyCreated);

		// ------------ Update ---------------
		specialtyCreated.setOffice(UP_OFFICE);
		specialtyCreated.setOpenHour(UP_OPEN_HOUR);
		specialtyCreated.setCloseHour(UP_CLOSE_HOUR);

		SpecialtyDTO specialtyUpdated = this.specialtyService.update(specialtyCreated);
		log.info(">>>> " + specialtyUpdated);

		// ------------ Validate ---------------
		SpecialtyDTO specialtyFound = null;
		try {
			specialtyFound = this.specialtyService.findById(specialtyCreated.getId());
		} catch (SpecialtyNotFoundException e) {
			fail(e.getMessage());
		}

		assertEquals(UP_OFFICE, specialtyFound.getOffice());
		assertEquals(UP_OPEN_HOUR, specialtyFound.getOpenHour());
		assertEquals(UP_CLOSE_HOUR, specialtyFound.getCloseHour());
	}

	/**
	 * Crea una especialidad, la elimina y comprueba que ya no existe
	 */
	@Test
	public void testDeleteSpecialty() {

		SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
				.name("ophthalmology")
				.office("Cayma")
				.openHour(10)
				.closeHour(18)
				.build();

		// ------------ Create ---------------
		SpecialtyDTO specialtyCreated = this.specialtyService.create(specialtyDTO);
		log.info("" + specialtyCreated);

		// ------------ Delete ---------------
		try {
			this.specialtyService.delete(specialtyCreated.getId());
		} catch (SpecialtyNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		assertThrows(SpecialtyNotFoundException.class,
				() -> this.specialtyService.findById(specialtyCreated.getId()));
	}

	/**
	 * Eliminar una especialidad que no existe debe lanzar excepcion
	 */
	@Test
	public void testDeleteSpecialtyNotFound() {

		assertThrows(SpecialtyNotFoundException.class, () -> this.specialtyService.delete(9999));
	}
}
