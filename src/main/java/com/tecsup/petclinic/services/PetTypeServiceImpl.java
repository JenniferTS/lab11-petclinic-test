package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mappers.PetTypeMapper;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {

	private final PetTypeRepository petTypeRepository;
	private final PetTypeMapper petTypeMapper;

	@Override
	public PetTypeDTO create(PetTypeDTO petTypeDTO) {
		PetType newType = petTypeRepository.save(petTypeMapper.mapToEntity(petTypeDTO));
		return petTypeMapper.mapToDto(newType);
	}

	@Override
	public PetTypeDTO update(PetTypeDTO petTypeDTO) {
		PetType updatedType = petTypeRepository.save(petTypeMapper.mapToEntity(petTypeDTO));
		return petTypeMapper.mapToDto(updatedType);
	}

	@Override
	public void delete(Integer id) throws PetTypeNotFoundException {
		PetTypeDTO type = findById(id);
		petTypeRepository.delete(petTypeMapper.mapToEntity(type));
	}

	@Override
	public PetTypeDTO findById(Integer id) throws PetTypeNotFoundException {
		Optional<PetType> type = petTypeRepository.findById(id);
		if (type.isEmpty())
			throw new PetTypeNotFoundException("Pet type not found...!");
		return petTypeMapper.mapToDto(type.get());
	}

	@Override
	public List<PetTypeDTO> findByName(String name) {
		List<PetType> types = petTypeRepository.findByName(name);
		types.forEach(type -> log.info("{}", type));
		return types.stream().map(petTypeMapper::mapToDto).toList();
	}

	@Override
	public List<PetTypeDTO> findByActive(Boolean active) {
		return petTypeRepository.findByActive(active).stream().map(petTypeMapper::mapToDto).toList();
	}

	@Override
	public List<PetTypeDTO> findAll() {
		return petTypeRepository.findAll().stream().map(petTypeMapper::mapToDto).toList();
	}
}
