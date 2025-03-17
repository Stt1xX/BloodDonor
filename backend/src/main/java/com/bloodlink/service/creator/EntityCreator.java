package com.bloodlink.service.creator;


import com.bloodlink.entities.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


public interface EntityCreator<T, DTOFrom> {

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    T fromDtoNew(DTOFrom dto, User caller);

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    T fromDtoUpdate(DTOFrom dto, User caller);
}
