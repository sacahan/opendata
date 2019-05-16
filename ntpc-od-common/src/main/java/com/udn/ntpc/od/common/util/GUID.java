package com.udn.ntpc.od.common.util;

import org.springframework.util.Assert;

import java.util.UUID;

/**
 * UUID 的一種，和 UUID 不同在於 GUID 是全大寫
 *
 */
public class GUID {

    private UUID uuid;
    
    public GUID(UUID uuid) {
        this.uuid = uuid;
    }
    
    public static GUID fromString(String name) {
        UUID uuid = UUID.fromString(name);
        return new GUID(uuid);
    }
    
    @Override
    public String toString() {
        Assert.notNull(uuid, "uuid can not be null");
        return uuid.toString().toUpperCase();
    }
    
}
