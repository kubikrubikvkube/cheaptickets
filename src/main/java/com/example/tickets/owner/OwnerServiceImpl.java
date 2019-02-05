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
    public Owner save(OwnerDTO dto) {
        Owner owner = mapper.fromDTO(dto);
        return repository.save(owner);
    }

    @Override
    public Owner save(Owner owner) {
        return repository.save(owner);
    }

    @Override
    public Optional<Owner> find(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Owner> find(String email) {
        return repository.findBy(email);
    }

    @Override
    public List<Owner> findAll() {
        Iterable<Owner> all = repository.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public void delete(String email) {
        Optional<Owner> ownerOpt = repository.findBy(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            repository.delete(owner);
        } else {
            var msg = String.format("Owner %s is not found", email);
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
    public Owner add(String email) {
        if (repository.findBy(email).isEmpty()) {
            Owner owner = new Owner();
            owner.setEmail(email);
            repository.save(owner);
        }

        Optional<Owner> foundOwner = repository.findBy(email);
        if (foundOwner.isEmpty()) {
            var msg = String.format("Problem saving owner %s", email);
            throw new ServiceException(msg);
        }
        return foundOwner.get();
    }

    @Override
    public long count() {
        return repository.count();
    }
}
