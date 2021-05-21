package com.mirai.hundun.cp.kcwiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiShipDetail;
import com.mirai.hundun.cp.kcwiki.feign.KcwikiApiFeignClient;
import com.mirai.hundun.service.file.IFileProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/05/20
 */
@Slf4j
@Component
public class KancolleWikiService implements IFileProvider {

    @Autowired
    KcwikiApiFeignClient apiFeignClient;
    
    String kancolleGameDataFolder =  "./data/images/kancolle_game_data";
    
    public String getStandardName(String fuzzyName) {
        
        return fuzzyName;
    }
    
    public KcwikiShipDetail getShipDetail(String fuzzyName) {
        String standardName = getStandardName(fuzzyName);
        KcwikiShipDetail shipDetail = apiFeignClient.shipDetail(standardName);
        return shipDetail;
    }
    
    @Override
    public InputStream download(String shipId) {
        
        File gameDataImage = findGameDataImage(shipId);
        if (gameDataImage != null) {
            try {
                return new FileInputStream(gameDataImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        return null;
    }
    
    public File findGameDataImage(String shipId) {
        String fourBitId = shipId;
        int numAddZero = 4 - shipId.length();
        for (int i = 0; i < numAddZero; i++) {
            fourBitId = "0" + fourBitId;
        }

        File folder = new File(kancolleGameDataFolder);
        if (folder.exists() && folder.isDirectory()) {
            File[] filesInFolder = folder.listFiles();
            for (File file : filesInFolder) {
                if (file.getName().startsWith(fourBitId + "_")) {
                   return file;
                }
            }
        }
        return null;
    }
    

    @Override
    public String getCacheSubFolderName() {
        return "kancolle_wiki";
    }

}
