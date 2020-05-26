package com.npht.springtodo.repository;

import com.npht.springtodo.model.ListOrder;

import org.springframework.data.repository.CrudRepository;

public interface ListOrderRepository extends CrudRepository<ListOrder, Long> {
    ListOrder findByProjectId(Long id);
}