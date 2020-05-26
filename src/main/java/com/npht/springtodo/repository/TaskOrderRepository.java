package com.npht.springtodo.repository;

import com.npht.springtodo.model.TaskOrder;

import org.springframework.data.repository.CrudRepository;

public interface TaskOrderRepository extends CrudRepository<TaskOrder, Long> {
    TaskOrder findByTaskId(Long taskId);
    TaskOrder findByListId(Long listId);
}