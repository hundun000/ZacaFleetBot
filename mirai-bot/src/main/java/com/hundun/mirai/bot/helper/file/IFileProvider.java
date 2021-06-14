package com.hundun.mirai.bot.helper.file;

import java.io.InputStream;

/**
 * @author hundun
 * Created on 2021/05/06
 */
public interface IFileProvider {
    
    InputStream download(String fileId);

    
    String getCacheSubFolderName();
}
