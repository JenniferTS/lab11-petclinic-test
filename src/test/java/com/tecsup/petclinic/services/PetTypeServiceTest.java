package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso 5 : Pruebas unitarias de la tabla types (tipos de mascotas)
 */
@SpringBootTest
@Slf4j
public class PetTypeServiceTest {

	@Autowired
	private PetTypeService petTypeService;

	/**
	 * Busca un tipo que existe en data.sql
	 */
	@Test
	public void testFindPetTypeById() {

		Integer ID = 2;
		String NAME_EXPECTED = "dog";
		PetTypeDTO type = null;

		try {
			type = this.petTypeService.findById(ID);
		} catch (PetTypeNotFoundException e) {
			fail(e.getMessage());
		}
		log.info("TYPE FOUND : " + type);

		assertEquals(NAME_EXPECTED, type.getName());
		assertEquals("Domestic canine", type.getDescription());
	}

	/**
	 * Busca un id que no existe, debe lanzar PetTypeNotFoundException
	 */
	@Test
	public void testFindPetTypeByIdNotFound() {

		assertThrows(PetTypeNotFoundException.class, () -> this.petTypeService.findById(9999));
	}

	/**
	 * Busca tipos por nombre
	 */
	@Test
	public void testFindPetTypeByName() {

		String NAME = "hamster";
		int SIZE_EXPECTED = 1;

		List<PetTypeDTO> types = this.petTypeService.findByName(NAME);

		assertEquals(SIZE_EXPECTED, types.size());
		assertEquals("low", types.get(0).getCareLevel());
	}

	/**
	 * Busca los tipos inactivos (en data.sql solo snake esta inactivo)
	 */
	@Test
	public void testFindInactivePetTypes() {

		List<PetTypeDTO> types = this.petTypeService.findByActive(false);

		assertFalse(types.isEmpty());
		assertTrue(types.stream().anyMatch(type -> type.getName().equals("snake")));
		types.forEach(type -> assertFalse(type.getActive()));
	}

	/**
	 * Crea un tipo de mascota nuevo
	 */
	@Test
	public void testCreatePetType() {

		String NAME = "guinea pig";
		String DESCRIPTION = "Small rodent from the Andes";
		Integer LIFESPAN = 6;

		PetTypeDTO typeDTO = PetTypeDTO.builder()
				.name(NAME)
				.description(DESCRIPTION)
				.active(true)
				.sizeCategory("small")
				.averageLifespan(LIFESPAN)
				.careLevel("low")
				.build();

		PetTypeDTO newType = this.petTypeService.create(typeDTO);
		log.info("TYPE CREATED : " + newType);

		assertNotNull(newType.getId());
		assertEquals(NAME, newType.getName());
		assertEquals(DESCRIPTION, newType.getDescription());
		assertEquals(LIFESPAN, newType.getAverageLifespan());
		assertTrue(newType.getActive());
	}

	/**
	 * Crea un tipo y actualiza su descripcion, nivel de cuidado y estado
	 */
	@Test
	public void testUpdatePetType() {

		PetTypeDTO typeDTO = PetTypeDTO.builder()
				.name("ferret")
				.description("Small carnivore")
				.active(true)
				.sizeCategory("small")
				.averageLifespan(7)
				.careLevel("medium")
				.build();

		String UP_DESCRIPTION = "Domestic ferret";
		String UP_CARE_LEVEL = "high";

		// ------------ Create ---------------
		PetTypeDTO typeCreated = this.petTypeService.create(typeDTO);
		log.info(">> " + typeCreated);

		// ------------ Update ---------------
		typeCreated.setDescription(UP_DESCRIPTION);
		typeCreated.setCareLevel(UP_CARE_LEVEL);
		typeCreated.setActive(false);

		PetTypeDTO typeUpdated = this.petTypeService.update(typeCreated);
		log.info(">>>> " + typeUpdated);

		// ------------ Validate ---------------
		PetTypeDTO typeFound = null;
		try {
			typeFound = this.petTypeService.findById(typeCreated.getId());
		} catch (PetTypeNotFoundException e) {
			fail(e.getMessage());
		}

		assertEquals(UP_DESCRIPTION, typeFound.getDescription());
		assertEquals(UP_CARE_LEVEL, typeFound.getCareLevel());
		assertFalse(typeFound.getActive());
	}

	/**
	 * Crea un tipo, lo elimina y comprueba que ya no existe
	 */
	@Test
	public void testDeletePetType() {

		PetTypeDTO typeDTO = PetTypeDTO.builder()
				.name("parrot")
				.description("Talking bird")
				.active(true)
				.sizeCategory("small")
				.averageLifespan(40)
				.careLevel("high")
				.build();

		// ------------ Create ---------------
		PetTypeDTO typeCreated = this.petTypeService.create(typeDTO);
		log.info("" + typeCreated);

		// ------------ Delete ---------------
		try {
			this.petTypeService.delete(typeCreated.getId());
		} catch (PetTypeNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		assertThrows(PetTypeNotFoundException.class, () -> this.petTypeService.findById(typeCreated.getId()));
	}

	/**
	 * Eliminar un tipo que no existe debe lanzar excepcion
	 */
	@Test
	public void testDeletePetTypeNotFound() {

		assertThrows(PetTypeNotFoundException.class, () -> this.petTypeService.delete(9999));
	}
}
