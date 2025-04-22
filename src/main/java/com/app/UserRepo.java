package com.app;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableMongoRepositories
public interface UserRepo extends MongoRepository<User, String> {

	User findByEmail(String email);

	User findByEmailAndPassword(String email, String password);

	List<User> findByUserType(String userType);
}
