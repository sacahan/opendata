package com.udn.ntpc.od.model.domain;

import java.util.ArrayList;

public class Parameters extends ArrayList<Parameter> {
    public static final Parameters EMPTY_PARAMETERS = new Parameters();
    
    private static final long serialVersionUID = 8556846737470197693L;

    public String[] getNames() {
        if (size() == 0) {
            return new String[]{};
        }
        String[] result = new String[size()];
        for (int idx = 0; idx < size(); idx++) {
            result[idx] = get(idx).getName();
        }
        return result;
    }

    public Object[] getValues() {
        if (size() == 0) {
            return new Object[]{};
        }
        Object[] result = new Object[size()];
        for (int idx = 0; idx < size(); idx++) {
            result[idx] = get(idx).getValue();
        }
        return result;
    }
    
}
