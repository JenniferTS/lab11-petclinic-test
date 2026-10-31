package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

	private final OwnerRepository ownerRepository;
	private final OwnerMapper ownerMapper;

	@Override
	public OwnerDTO create(OwnerDTO ownerDTO) {
		Owner newOwner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));
		return ownerMapper.mapToDto(newOwner);
	}

	@Override
	public OwnerDTO update(OwnerDTO ownerDTO) {
		Owner updatedOwner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));
		return ownerMapper.mapToDto(updatedOwner);
	}

	@Override
	public void delete(Integer id) throws OwnerNotFoundException {
		OwnerDTO owner = findById(id);
		ownerRepository.delete(ownerMapper.mapToEntity(owner));
	}

	@Override
	public OwnerDTO findById(Integer id) throws OwnerNotFoundException {
		Optional<Owner> owner = ownerRepository.findById(id);
		if (owner.isEmpty())
			throw new OwnerNotFoundException("Owner not found...!");
		return ownerMapper.mapToDto(owner.get());
	}

	@Override
	public List<OwnerDTO> findByLastName(String lastName) {
		List<Owner> owners = ownerRepository.findByLastName(lastName);
		owners.forEach(owner -> log.info("{}", owner));
		return owners.stream().map(ownerMapper::mapToDto).toList();
	}

	@Override
	public List<OwnerDTO> findByCity(String city) {
		return ownerRepository.findByCity(city).stream().map(ownerMapper::mapToDto).toList();
	}

	@Override
	public List<OwnerDTO> findAll() {
		return ownerRepository.findAll().stream().map(ownerMapper::mapToDto).toList();
	}
}
