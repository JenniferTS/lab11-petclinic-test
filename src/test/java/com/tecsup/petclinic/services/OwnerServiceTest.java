package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso 2 : Pruebas unitarias de la tabla owners
 */
@SpringBootTest
@Slf4j
public class OwnerServiceTest {

	@Autowired
	private OwnerService ownerService;

	/**
	 * Busca un dueno que existe en data.sql
	 */
	@Test
	public void testFindOwnerById() {

		Integer ID = 1;
		String FIRST_NAME = "George";
		String LAST_NAME = "Franklin";
		OwnerDTO owner = null;

		try {
			owner = this.ownerService.findById(ID);
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}
		log.info("OWNER FOUND : " + owner);

		assertEquals(FIRST_NAME, owner.getFirstName());
		assertEquals(LAST_NAME, owner.getLastName());
	}

	/**
	 * Busca un id que no existe, debe lanzar OwnerNotFoundException
	 */
	@Test
	public void testFindOwnerByIdNotFound() {

		assertThrows(OwnerNotFoundException.class, () -> this.ownerService.findById(9999));
	}

	/**
	 * Busca duenos por apellido (hay dos Davis en data.sql)
	 */
	@Test
	public void testFindOwnerByLastName() {

		String LAST_NAME = "Davis";
		int SIZE_EXPECTED = 2;

		List<OwnerDTO> owners = this.ownerService.findByLastName(LAST_NAME);

		assertEquals(SIZE_EXPECTED, owners.size());
	}

	/**
	 * Busca duenos por ciudad
	 */
	@Test
	public void testFindOwnerByCity() {

		String CITY = "Monona";
		int SIZE_EXPECTED = 2;

		List<OwnerDTO> owners = this.ownerService.findByCity(CITY);

		assertEquals(SIZE_EXPECTED, owners.size());
		owners.forEach(owner -> assertEquals(CITY, owner.getCity()));
	}

	/**
	 * Crea un dueno nuevo
	 */
	@Test
	public void testCreateOwner() {

		String FIRST_NAME = "Carmen";
		String LAST_NAME = "Valdivia";
		String CITY = "Arequipa";

		OwnerDTO ownerDTO = OwnerDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.address("Av. Ejercito 123")
				.city(CITY)
				.telephone("054223344")
				.build();

		OwnerDTO newOwner = this.ownerService.create(ownerDTO);
		log.info("OWNER CREATED : " + newOwner);

		assertNotNull(newOwner.getId());
		assertEquals(FIRST_NAME, newOwner.getFirstName());
		assertEquals(LAST_NAME, newOwner.getLastName());
		assertEquals(CITY, newOwner.getCity());
	}

	/**
	 * Crea un dueno y actualiza su direccion, ciudad y telefono
	 */
	@Test
	public void testUpdateOwner() {

		OwnerDTO ownerDTO = OwnerDTO.builder()
				.firstName("Luis")
				.lastName("Chavez")
				.address("Calle Mercaderes 200")
				.city("Arequipa")
				.telephone("054111222")
				.build();

		String UP_ADDRESS = "Av. La Cultura 450";
		String UP_CITY = "Cusco";
		String UP_TELEPHONE = "084999888";

		// ------------ Create ---------------
		OwnerDTO ownerCreated = this.ownerService.create(ownerDTO);
		log.info(">> " + ownerCreated);

		// ------------ Update ---------------
		ownerCreated.setAddress(UP_ADDRESS);
		ownerCreated.setCity(UP_CITY);
		ownerCreated.setTelephone(UP_TELEPHONE);

		OwnerDTO ownerUpdated = this.ownerService.update(ownerCreated);
		log.info(">>>> " + ownerUpdated);

		// ------------ Validate ---------------
		OwnerDTO ownerFound = null;
		try {
			ownerFound = this.ownerService.findById(ownerCreated.getId());
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}

		assertEquals(UP_ADDRESS, ownerFound.getAddress());
		assertEquals(UP_CITY, ownerFound.getCity());
		assertEquals(UP_TELEPHONE, ownerFound.getTelephone());
	}

	/**
	 * Crea un dueno, lo elimina y comprueba que ya no existe
	 */
	@Test
	public void testDeleteOwner() {

		OwnerDTO ownerDTO = OwnerDTO.builder()
				.firstName("Ana")
				.lastName("Flores")
				.address("Jr. Moral 15")
				.city("Arequipa")
				.telephone("054777666")
				.build();

		// ------------ Create ---------------
		OwnerDTO ownerCreated = this.ownerService.create(ownerDTO);
		log.info("" + ownerCreated);

		// ------------ Delete ---------------
		try {
			this.ownerService.delete(ownerCreated.getId());
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		assertThrows(OwnerNotFoundException.class, () -> this.ownerService.findById(ownerCreated.getId()));
	}

	/**
	 * Eliminar un dueno que no existe debe lanzar excepcion
	 */
	@Test
	public void testDeleteOwnerNotFound() {

		assertThrows(OwnerNotFoundException.class, () -> this.ownerService.delete(9999));
	}
}
