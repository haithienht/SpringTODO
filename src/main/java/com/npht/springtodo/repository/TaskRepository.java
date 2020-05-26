package com.npht.springtodo.repository;

import com.npht.springtodo.model.Task;

import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}