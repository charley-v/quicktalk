package com.quicktalk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quicktalk.entity.Users;
import com.quicktalk.projection.UserProjection;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	List<UserProjection> findAllProjectedBy();
}
