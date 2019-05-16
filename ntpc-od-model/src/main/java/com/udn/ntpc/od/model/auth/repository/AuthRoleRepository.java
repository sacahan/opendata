package com.udn.ntpc.od.model.auth.repository;

import com.udn.ntpc.od.model.auth.domain.AuthRole;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRoleRepository extends CustomRespository<AuthRole, String> {

}
