package com.npht.springtodo.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.npht.springtodo.model.User;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmailAndPassword(String email, String password);
}
