package com.udn.ntpc.od.frontend.entity.repository;

import com.udn.ntpc.od.frontend.entity.domain.MockUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockUserRepository extends JpaRepository<MockUser, Integer> {
}
