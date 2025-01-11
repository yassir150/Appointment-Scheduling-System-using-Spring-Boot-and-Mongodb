package com.app;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableMongoRepositories
public interface AdminRepo extends MongoRepository<Admin, String> {

	Admin findByEmailAndPassword(String email, String password);

}
