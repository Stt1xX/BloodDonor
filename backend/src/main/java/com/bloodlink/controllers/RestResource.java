package com.bloodlink.controllers;


import com.bloodlink.service.EntityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public abstract class RestResource<T, DTOFrom, ID extends Serializable> {

    private final EntityService<T, DTOFrom, ID> entityService;

    public RestResource(EntityService<T, DTOFrom, ID> entityService) {
        this.entityService = entityService;
    }

    @PostMapping(value = "")
    public String postResource(@RequestBody DTOFrom dto, HttpServletResponse response) {
        entityService.createEntity(dto);
        return "Created";
    }

    @PutMapping(value = "")
    public String putResource(@RequestBody DTOFrom dto, HttpServletResponse response) {
        entityService.updateEntity(dto);
        return "Updated";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteResource(@PathVariable ID id, HttpServletResponse response) {
        entityService.deleteEntity(id);
        return "Deleted";
    }

}
