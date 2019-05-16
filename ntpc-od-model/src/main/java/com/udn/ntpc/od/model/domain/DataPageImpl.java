package com.udn.ntpc.od.model.domain;

import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataPageImpl<T> extends PageImpl<T> implements DataPage<T> {
    private static final long serialVersionUID = 2074737377766062449L;

    private DataFieldCfgsDto dataFieldCfgs = DataFieldCfgsDto.EMPTY_FIELD_CFGS;
    
	private TableFields fields;
	
	public DataPageImpl(List<T> content) {
        super(content);
	}

    public DataPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public DataPageImpl(List<T> content, Pageable pageable, long total, DataFieldCfgsDto dataFieldCfgsDto) {
        super(content, pageable, total);
        this.setDataFieldCfgs(dataFieldCfgsDto);
    }
    
    public DataPageImpl(List<T> content, Pageable pageable, long total, TableFields fields, DataFieldCfgsDto dataFieldCfgsDto) {
        super(content, pageable, total);
        this.setFields(fields);
        this.setDataFieldCfgs(dataFieldCfgsDto);
    }

	public DataPageImpl(List<T> content, Pageable pageable, long total, TableFields fields) {
        super(content, pageable, total);
	    this.setFields(fields);
	}

}
