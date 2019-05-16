package com.udn.ntpc.od.common.variables;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class CustomResult<R> {
    
    private boolean isSuccess;
    private R result;
    
    public static <T extends Object> CustomResult<T> result(boolean isSuccess, T result) {
        return new CustomResult<>(isSuccess, result);
    }
    
    public CustomResult(boolean isSuccess, R result) {
        this.isSuccess = isSuccess;
        this.result = result;
    }
    
    public boolean hasValue() {
        if (result instanceof Collection<?>) {
            return isSuccess && ((result != null) && !((Collection<?>) result).isEmpty());
        }
        return isSuccess && (result != null);
    }

}
