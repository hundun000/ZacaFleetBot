package hundun.zacafleetbot.mirai.botlogic.helper.file;

import java.io.File;
import java.io.InputStream;

/**
 * @author hundun
 * Created on 2021/05/06
 */
public interface IFileOperationDelegator {
    
    InputStream downloadOrFromLocal(String fileId, File localDataFolder);

    String getCacheSubFolderName();
    
    File fromCacheOrDownloadOrFromLocal(String fileId, File rootCacheFolder, File localDataFolder);
}
