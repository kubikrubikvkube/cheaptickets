package com.example.tickets.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository repository;

    public OwnerServiceImpl(OwnerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Owner add(String name, String email) {
        if (!repository.existsByName(name)) {
            OwnerDTO ownerDTO = new OwnerDTO();
            ownerDTO.setName(name);
            ownerDTO.setEmail(email);
            Owner newOwner = OwnerDTOMapper.INSTANCE.fromDTO(ownerDTO);
            repository.save(newOwner);
        }
        return repository.findBy(name);
    }

    @Override
    public Owner get(String name) {
        return repository.findBy(name);
    }

    @Override
    public void delete(String name) {
        Owner owner = repository.findBy(name);
        repository.delete(owner);
    }
}
