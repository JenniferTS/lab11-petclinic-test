package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

	private final VetSpecialtyRepository vetSpecialtyRepository;
	private final VetSpecialtyMapper vetSpecialtyMapper;

	@Override
	public VetSpecialtyDTO assign(VetSpecialtyDTO dto) throws VetSpecialtyAlreadyExistsException {

		VetSpecialtyId id = new VetSpecialtyId(dto.getVetId(), dto.getSpecialtyId());

		if (vetSpecialtyRepository.existsById(id))
			throw new VetSpecialtyAlreadyExistsException("The vet already has this specialty...!");

		VetSpecialty saved = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(dto));
		return vetSpecialtyMapper.mapToDto(saved);
	}

	@Override
	public VetSpecialtyDTO update(VetSpecialtyDTO dto) throws VetSpecialtyNotFoundException {

		findById(dto.getVetId(), dto.getSpecialtyId());

		VetSpecialty saved = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(dto));
		return vetSpecialtyMapper.mapToDto(saved);
	}

	@Override
	public void remove(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException {

		findById(vetId, specialtyId);
		vetSpecialtyRepository.deleteById(new VetSpecialtyId(vetId, specialtyId));
	}

	@Override
	public VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException {

		Optional<VetSpecialty> vetSpecialty = vetSpecialtyRepository.findById(new VetSpecialtyId(vetId, specialtyId));

		if (vetSpecialty.isEmpty())
			throw new VetSpecialtyNotFoundException("Vet specialty not found...!");

		return vetSpecialtyMapper.mapToDto(vetSpecialty.get());
	}

	@Override
	public List<VetSpecialtyDTO> findSpecialtiesByVet(Integer vetId) {
		List<VetSpecialty> list = vetSpecialtyRepository.findByVetId(vetId);
		list.forEach(vs -> log.info("{}", vs));
		return list.stream().map(vetSpecialtyMapper::mapToDto).toList();
	}

	@Override
	public List<VetSpecialtyDTO> findVetsBySpecialty(Integer specialtyId) {
		List<VetSpecialty> list = vetSpecialtyRepository.findBySpecialtyId(specialtyId);
		list.forEach(vs -> log.info("{}", vs));
		return list.stream().map(vetSpecialtyMapper::mapToDto).toList();
	}
}
