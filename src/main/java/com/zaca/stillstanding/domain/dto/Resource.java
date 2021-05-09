package com.zaca.stillstanding.domain.dto;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class Resource {
    private ResourceType type;
    private String data;
    
    public Resource(String localFilePathName) {
        this.type = ResourceType.getByLocalFileExtension(localFilePathName);
        this.data = localFilePathName;
    }
    
    public Resource() {
        
    }
}
