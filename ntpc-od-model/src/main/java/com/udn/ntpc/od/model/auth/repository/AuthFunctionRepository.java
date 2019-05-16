package com.udn.ntpc.od.model.auth.repository;

import com.udn.ntpc.od.model.auth.domain.AuthFunction;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthFunctionRepository extends CustomRespository<AuthFunction, String> {

}
