package com.microservico.msuser.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.microservico.msuser.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    List<User> findByName(String value);
 
    List<User> findByUsername(String value);
    
}
