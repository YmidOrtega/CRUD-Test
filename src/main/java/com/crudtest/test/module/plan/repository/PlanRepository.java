package com.crudtest.test.module.plan.repository;

import com.crudtest.test.module.plan.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
