package com.udn.ntpc.od.common.converter;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class AbstractObjectConverter<S, T> extends org.modelmapper.AbstractConverter<S, T> implements Converter<S, T> {

    protected ModelMapper modelMapper = new ModelMapper();
    protected Class<T> targetClass;

    @PostConstruct
    private void postConstruct() {
        log.debug(getClass().getName() + " constructed");
    }

    protected void doPostConstruct() {
        this.targetClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true); // 修正 boolean temp 會 mapping 錯問題
    }

    protected void beforeConvert(S source) {
    }

    @Override
    public T convert(S source) {
        beforeConvert(source);
        return doConvert(source);
    }

    protected T doConvert(final S source) {
        return modelMapper.map(source, targetClass);
    }

}
