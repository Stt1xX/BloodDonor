package com.bloodlink.service;


import com.bloodlink.entities.Ownable;
import com.bloodlink.exceptions.AuthorizationException;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.service.creator.EntityCreator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public abstract class OwnableEntityService<T extends Ownable, DTOFrom, ID extends Serializable> extends EntityService<T,
        DTOFrom, ID> {

    protected OwnableEntityService(ResourceUtilsService resourceUtils, JpaRepository<T, ID> repository,
                                   EntityCreator<T, DTOFrom> entityCreatorService) {
        super(resourceUtils, repository, entityCreatorService);
    }

    public void deleteEntity(ID id) {
        var caller = resourceUtils.getCaller();
        var resourceOpt = repository.findById(id);
        if (resourceOpt.isEmpty()) {
            throw new ResourceNotFoundException("Failed to find resource with id " + id);
        }
        var resource = resourceOpt.get();
        if (resource.getOwner().getEmail().equals(caller.getEmail())) {
            repository.delete(resource);
        } else {
            throw new AuthorizationException("Caller doesn't own resource requested for deletion");
        }

    }

}
