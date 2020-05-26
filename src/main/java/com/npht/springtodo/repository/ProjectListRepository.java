package com.npht.springtodo.repository;

import com.npht.springtodo.model.ProjectList;

import org.springframework.data.repository.CrudRepository;

public interface ProjectListRepository extends CrudRepository<ProjectList, Long> {

}