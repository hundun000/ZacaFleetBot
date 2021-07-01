package hundun.zacafleetbot.mirai.botlogic.helper.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import hundun.zacafleetbot.mirai.botlogic.helper.Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/05/06
 */
@Slf4j
public class FileOperationDelegate {
    
    

    IFileOperationDelegator provider;
    
    public FileOperationDelegate(IFileOperationDelegator delegator) {
        this.provider = delegator;
    }


    
    private File getCacheFile(String fileId, File cacheFolder) {
        String subFolerName = provider.getCacheSubFolderName();
        Utils.checkFolder(subFolerName, cacheFolder.getAbsolutePath());
        String saveFilePathName = cacheFolder.getAbsolutePath() + subFolerName + "/" + fileId;
        File file = new File(saveFilePathName);
        
        return file;
    }

    public File downloadOrFromCache(String fileId, File cacheFolder, File rawDataFolder) {
        String subFolerName = provider.getCacheSubFolderName();
        File file = getCacheFile(fileId, cacheFolder);
        if (file.exists()) {
            log.debug("image from cache :{}", subFolerName + "---" + fileId);
        } else {
            InputStream inputStream = provider.download(fileId, rawDataFolder);
            
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
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(outBytes);
                fos.close();
                log.info("FileOutputStream success: {}", fileId);
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
