package com.chirak.cbs.repository;

import com.chirak.cbs.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student,  Long> {
    Optional<Student> findByEmail(String email);
}
