package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

	private final SpecialtyRepository specialtyRepository;
	private final SpecialtyMapper specialtyMapper;

	@Override
	public SpecialtyDTO create(SpecialtyDTO specialtyDTO) {
		Specialty newSpecialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));
		return specialtyMapper.mapToDto(newSpecialty);
	}

	@Override
	public SpecialtyDTO update(SpecialtyDTO specialtyDTO) {
		Specialty updated = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));
		return specialtyMapper.mapToDto(updated);
	}

	@Override
	public void delete(Integer id) throws SpecialtyNotFoundException {
		SpecialtyDTO specialty = findById(id);
		specialtyRepository.delete(specialtyMapper.mapToEntity(specialty));
	}

	@Override
	public SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException {
		Optional<Specialty> specialty = specialtyRepository.findById(id);
		if (specialty.isEmpty())
			throw new SpecialtyNotFoundException("Specialty not found...!");
		return specialtyMapper.mapToDto(specialty.get());
	}

	@Override
	public List<SpecialtyDTO> findByName(String name) {
		List<Specialty> specialties = specialtyRepository.findByName(name);
		specialties.forEach(specialty -> log.info("{}", specialty));
		return specialties.stream().map(specialtyMapper::mapToDto).toList();
	}

	@Override
	public List<SpecialtyDTO> findByOffice(String office) {
		return specialtyRepository.findByOffice(office).stream().map(specialtyMapper::mapToDto).toList();
	}

	@Override
	public List<SpecialtyDTO> findAll() {
		return specialtyRepository.findAll().stream().map(specialtyMapper::mapToDto).toList();
	}
}
