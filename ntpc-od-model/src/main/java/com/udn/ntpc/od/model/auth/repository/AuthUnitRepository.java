package com.udn.ntpc.od.model.auth.repository;

import com.udn.ntpc.od.model.auth.domain.AuthUnit;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUnitRepository extends CustomRespository<AuthUnit, String> {

}
