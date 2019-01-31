package com.example.tickets.owner;

import com.example.tickets.util.ServiceException;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {
    private final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository repository;
    private final OwnerDTOMapper mapper;

    public OwnerServiceImpl(OwnerRepository repository, OwnerDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Owner add(String ownerName, String email) {
        if (!repository.existsByName(ownerName)) {
            OwnerDTO ownerDTO = new OwnerDTO();
            ownerDTO.setName(ownerName);
            ownerDTO.setEmail(email);
            Owner newOwner = OwnerDTOMapper.INSTANCE.fromDTO(ownerDTO);
            repository.save(newOwner);
        }
        Optional<Owner> ownerOpt = repository.findBy(ownerName);
        if (ownerOpt.isPresent()) {
            return ownerOpt.get();
        } else {
            var msg = String.format("Can't add owner %s with email %s", ownerName, email);
            throw new ServiceException(msg);
        }

    }

    @Override
    public Owner save(OwnerDTO dto) {
        Owner owner = mapper.fromDTO(dto);
        return repository.save(owner);
    }

    @Override
    public Optional<Owner> find(String name) {
        return repository.findBy(name);
    }

    @Override
    public List<Owner> findAll() {
        Iterable<Owner> all = repository.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public void delete(String name) {
        Optional<Owner> ownerOpt = repository.findBy(name);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            repository.delete(owner);
        } else {
            var msg = String.format("Owner %s is not found", name);
            throw new ServiceException(msg);
        }

    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Override
    public Owner add(String name) {
        if (repository.findBy(name).isEmpty()) {
            Owner owner = new Owner();
            owner.setName(name);
            repository.save(owner);
        }

        Optional<Owner> foundOwner = repository.findBy(name);
        if (foundOwner.isEmpty()) {
            var msg = String.format("Problem saving owner %s", name);
            throw new ServiceException(msg);
        }
        return foundOwner.get();
    }

    @Override
    public long count() {
        return repository.count();
    }
}
