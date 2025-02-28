package com.abhi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abhi.entity.UserCredential;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {

	 Optional <UserCredential> findByName(String name);
}
