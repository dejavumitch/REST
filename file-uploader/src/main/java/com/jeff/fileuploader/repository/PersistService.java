package com.jeff.fileuploader.repository;

import com.jeff.fileuploader.module.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistService extends JpaRepository<MetaData, Integer> {
}
