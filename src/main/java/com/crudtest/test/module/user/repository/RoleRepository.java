package com.crudtest.test.module.user.repository;

import com.crudtest.test.module.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
