package com.mirai.hundun.cp;

import java.io.InputStream;
import java.util.Map;

import com.mirai.hundun.service.file.IFileProvider;

/**
 * @author hundun
 * Created on 2021/05/20
 */
public class KancolleWikiService implements IFileProvider {

    
    
    public String getStandardName(String fuzzyName) {
        return fuzzyName;
    }
    
    
    @Override
    public InputStream download(String fileId) {
        return null;
    }

    @Override
    public String getCacheSubFolderName() {
        return "kancolle_wiki";
    }

}
