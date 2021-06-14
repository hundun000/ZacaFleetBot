package com.hundun.mirai.bot.helper.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.hundun.mirai.bot.core.IManualWired;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/05/06
 */
@Slf4j
public class FileService implements IManualWired {
    
    private static final String RESOURCE_DOWNLOAD_FOLDER = "file_cache/";
    
    @Override
    public void manualWired() {
        // TODO Auto-generated method stub
        
    }
    
    private void checkFolder(String subFolerName) {
        File directory = new File(RESOURCE_DOWNLOAD_FOLDER);
        if (! directory.exists()){
            directory.mkdir();
        }
        
        File subFoler = new File(RESOURCE_DOWNLOAD_FOLDER + subFolerName);
        if (! subFoler.exists()){
            subFoler.mkdir();
        }
    }
    
    
    public boolean fileCacheExists(String fileId, IFileProvider provider) {
        String saveFilePathName = getCacheFilePath(fileId, provider);
        File file = new File(saveFilePathName);
        return file.exists();
    }
    
    private String getCacheFilePath(String fileId, IFileProvider provider) {
        String subFolerName = provider.getCacheSubFolderName();
        checkFolder(subFolerName);
        String saveFilePathName = RESOURCE_DOWNLOAD_FOLDER + subFolerName + "/" + fileId;
        
        
        return saveFilePathName;
    }

    public File downloadOrFromCache(String fileId, IFileProvider provider) {
        String subFolerName = provider.getCacheSubFolderName();
        String saveFilePathName = getCacheFilePath(fileId, provider);
        File file = new File(saveFilePathName);
        if (file.exists()) {
            log.info("image from cache :{}", subFolerName + "---" + fileId);
        } else {
            InputStream inputStream = provider.download(fileId);
            
            if (inputStream == null) {
                log.info("provider not support download, image null for: {}", subFolerName + "---" + fileId);
                return null;
            }
            
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1!=(n=inputStream.read(buf)))
                {
                   out.write(buf, 0, n);
                }
                out.close();
                inputStream.close();
                byte[] outBytes = out.toByteArray();
                FileOutputStream fos = new FileOutputStream(saveFilePathName);
                fos.write(outBytes);
                fos.close();
                log.info("FileOutputStream success: {}", fileId);
                file = new File(saveFilePathName);
            } catch (Exception e) {
                log.info("FileOutputStream faild {} {}", fileId, e);
                return null;
            }

            if (file != null && file.exists()) {
                log.info("image from download and success :{}", subFolerName + "---" + fileId);
            } else {
                log.warn("image from download but fail :{}", subFolerName + "---" + fileId);
            }
        }
        return file;
    }


    
    
    
    
}
