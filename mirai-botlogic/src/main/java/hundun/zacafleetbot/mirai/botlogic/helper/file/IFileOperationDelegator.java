package hundun.zacafleetbot.mirai.botlogic.helper.file;

import java.io.File;
import java.io.InputStream;

/**
 * @author hundun
 * Created on 2021/05/06
 */
public interface IFileOperationDelegator {
    
    InputStream download(String fileId, File rawDataFolder);

    String getCacheSubFolderName();
    
    File downloadOrFromCache(String fileId, File cacheFolder, File rawDataFolder);
}
