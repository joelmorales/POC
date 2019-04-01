package com.example.dataproviders.database.persistance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.dataproviders.database.domain.StatusTable;

@Repository
public interface StatusDAO extends CrudRepository<StatusTable,Long> {

}
