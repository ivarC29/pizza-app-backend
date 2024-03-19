package com.ivarc29.pizza.persistence.repository;

import com.ivarc29.pizza.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {
}
