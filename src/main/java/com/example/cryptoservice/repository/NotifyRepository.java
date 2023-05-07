package com.example.cryptoservice.repository;

import com.example.cryptoservice.entity.Notify;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends ListCrudRepository<Notify, Long> {

}
