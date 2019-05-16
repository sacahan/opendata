package com.udn.ntpc.od.core.service;

import java.io.Serializable;
import java.util.stream.Stream;

public interface CustomService<T, ID extends Serializable> extends CommonService<T, ID> {

    Stream<T> streamFindAll();

}
