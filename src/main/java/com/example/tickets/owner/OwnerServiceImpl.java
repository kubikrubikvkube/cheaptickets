package com.example.tickets.owner;

import com.example.tickets.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {
    private final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository repository;

    public OwnerServiceImpl(OwnerRepository repository) {
        this.repository = repository;
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
    public Optional<Owner> get(String name) {
        return repository.findBy(name);
    }

    @Override
    public void delete(String name) {
        Optional<Owner> ownerOpt = repository.findBy(name);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            repository.delete(owner);
        } else {
            var msg = String.format("Owner %s is not found");
            throw new ServiceException(msg);
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
}
