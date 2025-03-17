package com.bloodlink.service;

import com.bloodlink.service.creator.EntityCreator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public abstract class EntityService<T, DTOFrom, ID extends Serializable> {

    protected final ResourceUtilsService resourceUtils;
    protected final JpaRepository<T, ID> repository;
    protected final EntityCreator<T, DTOFrom> entityCreatorService;

    protected EntityService(ResourceUtilsService resourceUtils, JpaRepository<T, ID> repository, EntityCreator<T, DTOFrom> entityCreatorService) {
        this.resourceUtils = resourceUtils;
        this.repository = repository;
        this.entityCreatorService = entityCreatorService;
    }

    public T createEntity(DTOFrom dto) {
        var owner = resourceUtils.getCaller();
        var resource = entityCreatorService.fromDtoNew(dto, owner);
        repository.save(resource);
        return resource;
    }

    public void updateEntity(DTOFrom dto) {
        var caller = resourceUtils.getCaller();
        var resource = entityCreatorService.fromDtoUpdate(dto, caller);
        repository.save(resource);
    }

    public abstract void deleteEntity(ID id);

}
