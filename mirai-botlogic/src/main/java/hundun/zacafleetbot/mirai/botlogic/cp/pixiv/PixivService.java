package hundun.zacafleetbot.mirai.botlogic.cp.pixiv;

import java.io.File;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import feign.Response;
import hundun.zacafleetbot.mirai.botlogic.cp.pixiv.feign.RainchanPixivFeignClient;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.WeiboService;
import hundun.zacafleetbot.mirai.botlogic.helper.file.FileOperationDelegate;
import hundun.zacafleetbot.mirai.botlogic.helper.file.IFileOperationDelegator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/07/21
 */
@Slf4j
@Service
public class PixivService implements IFileOperationDelegator {
    
    FileOperationDelegate fileOperationDelegate;
    
    @Autowired
    RainchanPixivFeignClient rainchanPixivFeignClient;
    
    public PixivService() {
        this.fileOperationDelegate = new FileOperationDelegate(this);
    }
    
    @PostConstruct
    public void test() {
        final Response response = rainchanPixivFeignClient.randomSmallPictures();
        System.out.println();
    }
    
    
    @Override
    public InputStream downloadOrFromLocal(String fileId, File cacheFolder) {
        try {
            final Response response = rainchanPixivFeignClient.randomSmallPictures();
            final Response.Body body = response.body();
            final InputStream inputStream = body.asInputStream();
            return inputStream;
        } catch (Exception e) {
            log.info("download image faild {} {}", fileId, e);
            return null;
        }
    }

    @Override
    public String getCacheSubFolderName() {
        return "pixiv";
    }

    @Override
    public File fromCacheOrDownloadOrFromLocal(String fileId, File cacheFolder, File rawDataFolder) {
        return fileOperationDelegate.fromCacheOrDownloadOrFromLocal(fileId, cacheFolder, rawDataFolder);
    }

}
