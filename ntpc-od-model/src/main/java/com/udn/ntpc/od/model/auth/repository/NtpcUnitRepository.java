package com.udn.ntpc.od.model.auth.repository;

import com.udn.ntpc.od.model.auth.domain.NtpcUnit;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface NtpcUnitRepository extends CustomRespository<NtpcUnit, String> {

}
