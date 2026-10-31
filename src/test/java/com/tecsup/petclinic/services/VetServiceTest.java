package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso 1 : Pruebas unitarias de la tabla vets
 */
@SpringBootTest
@Slf4j
public class VetServiceTest {

	@Autowired
	private VetService vetService;

	/**
	 * Busca un veterinario que existe en data.sql
	 */
	@Test
	public void testFindVetById() {

		Integer ID = 1;
		String FIRST_NAME_EXPECTED = "James";
		String LAST_NAME_EXPECTED = "Carter";

		VetDTO vet = null;

		try {
			vet = this.vetService.findById(ID);
		} catch (VetNotFoundException e) {
			fail(e.getMessage());
		}
		log.info("VET FOUND : " + vet);

		assertEquals(FIRST_NAME_EXPECTED, vet.getFirstName());
		assertEquals(LAST_NAME_EXPECTED, vet.getLastName());
	}

	/**
	 * Busca un id que no existe, debe lanzar VetNotFoundException
	 */
	@Test
	public void testFindVetByIdNotFound() {

		Integer ID = 9999;

		assertThrows(VetNotFoundException.class, () -> this.vetService.findById(ID));
	}

	/**
	 * Busca veterinarios por apellido
	 */
	@Test
	public void testFindVetByLastName() {

		String LAST_NAME = "Douglas";
		int SIZE_EXPECTED = 1;

		List<VetDTO> vets = this.vetService.findByLastName(LAST_NAME);

		assertEquals(SIZE_EXPECTED, vets.size());
		assertEquals("Linda", vets.get(0).getFirstName());
	}

	/**
	 * Busca veterinarios inactivos (en data.sql solo hay uno)
	 */
	@Test
	public void testFindInactiveVets() {

		List<VetDTO> vets = this.vetService.findByActive(false);

		assertFalse(vets.isEmpty());
		vets.forEach(vet -> assertFalse(vet.getActive()));
	}

	/**
	 * Crea un veterinario nuevo
	 */
	@Test
	public void testCreateVet() {

		String FIRST_NAME = "Lucia";
		String LAST_NAME = "Mendoza";
		String EMAIL = "lucia.mendoza@petclinic.com";

		VetDTO vetDTO = VetDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.email(EMAIL)
				.phone("959111222")
				.active(true)
				.build();

		VetDTO newVetDTO = this.vetService.create(vetDTO);
		log.info("VET CREATED : " + newVetDTO);

		assertNotNull(newVetDTO.getId());
		assertEquals(FIRST_NAME, newVetDTO.getFirstName());
		assertEquals(LAST_NAME, newVetDTO.getLastName());
		assertEquals(EMAIL, newVetDTO.getEmail());
		assertTrue(newVetDTO.getActive());
	}

	/**
	 * Crea un veterinario y luego actualiza sus datos
	 */
	@Test
	public void testUpdateVet() {

		VetDTO vetDTO = VetDTO.builder()
				.firstName("Mario")
				.lastName("Quispe")
				.email("mario.quispe@petclinic.com")
				.phone("959333444")
				.active(true)
				.build();

		String UP_LAST_NAME = "Quispe Huaman";
		String UP_PHONE = "959000111";

		// ------------ Create ---------------
		VetDTO vetCreated = this.vetService.create(vetDTO);
		log.info(">> " + vetCreated);

		// ------------ Update ---------------
		vetCreated.setLastName(UP_LAST_NAME);
		vetCreated.setPhone(UP_PHONE);
		vetCreated.setActive(false);

		VetDTO vetUpdated = this.vetService.update(vetCreated);
		log.info(">>>> " + vetUpdated);

		// ------------ Validate ---------------
		VetDTO vetFound = null;
		try {
			vetFound = this.vetService.findById(vetCreated.getId());
		} catch (VetNotFoundException e) {
			fail(e.getMessage());
		}

		assertEquals(UP_LAST_NAME, vetFound.getLastName());
		assertEquals(UP_PHONE, vetFound.getPhone());
		assertFalse(vetFound.getActive());
	}

	/**
	 * Crea un veterinario, lo elimina y comprueba que ya no existe
	 */
	@Test
	public void testDeleteVet() {

		VetDTO vetDTO = VetDTO.builder()
				.firstName("Rosa")
				.lastName("Paredes")
				.email("rosa.paredes@petclinic.com")
				.phone("959555666")
				.active(true)
				.build();

		// ------------ Create ---------------
		VetDTO vetCreated = this.vetService.create(vetDTO);
		log.info("" + vetCreated);

		// ------------ Delete ---------------
		try {
			this.vetService.delete(vetCreated.getId());
		} catch (VetNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		assertThrows(VetNotFoundException.class, () -> this.vetService.findById(vetCreated.getId()));
	}

	/**
	 * Eliminar un veterinario que no existe debe lanzar excepcion
	 */
	@Test
	public void testDeleteVetNotFound() {

		assertThrows(VetNotFoundException.class, () -> this.vetService.delete(9999));
	}
}
