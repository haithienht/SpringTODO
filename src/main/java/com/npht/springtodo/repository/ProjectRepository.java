package com.npht.springtodo.repository;

import java.util.List;

import com.npht.springtodo.model.Project;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    // @Query("SELECT p FROM project p WHERE p.user_project_id = ?1")
    List<Project> findByUser_Id(Long id);
    List<Project> findByUser_Email(String email);
}