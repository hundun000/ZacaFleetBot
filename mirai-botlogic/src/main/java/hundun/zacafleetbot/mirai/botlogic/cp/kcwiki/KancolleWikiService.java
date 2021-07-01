package hundun.zacafleetbot.mirai.botlogic.cp.kcwiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.dto.KcwikiInitEquip;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.dto.KcwikiShipDetail;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.model.ShipInfo;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.model.ShipUpgradeLink;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.feign.KcwikiApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.helper.file.FileOperationDelegate;
import hundun.zacafleetbot.mirai.botlogic.helper.file.IFileOperationDelegator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/05/20
 */
@Slf4j
@Component
public class KancolleWikiService implements IFileOperationDelegator {

    @Autowired
    KcwikiApiFeignClient apiFeignClient;
    
    public static String kancolleGameDataSubFolder =  "images/kancolle_game_data/";
    
    FileOperationDelegate fileOperationDelegate;
    
    public KancolleWikiService() {
        this.fileOperationDelegate = new FileOperationDelegate(this);
    }
    
    public String getStandardName(String fuzzyName) {
        
        return fuzzyName;
    }
    
    public KcwikiShipDetail getShipDetail(String fuzzyName) {
        String standardName = getStandardName(fuzzyName);
        KcwikiShipDetail shipDetail = apiFeignClient.shipDetail(standardName);
        return shipDetail;
    }
    
    public ShipUpgradeLink getShipUpgradeLine(String fuzzyName) {
        ShipUpgradeLink upgradeLink = new ShipUpgradeLink();
        String standardName = getStandardName(fuzzyName);
        KcwikiShipDetail currentdetail = apiFeignClient.shipDetail(standardName); 
        while (currentdetail != null && currentdetail.getId() > 0) {
            if (!upgradeLink.getUpgradeLinkIds().contains(currentdetail.getId())) {
                upgradeLink.getUpgradeLinkIds().add(Integer.valueOf(currentdetail.getId()));
                KcwikiInitEquip initEquip = apiFeignClient.initEquip(currentdetail.getId());
                ShipInfo shipInfo = new ShipInfo(currentdetail, initEquip);
                upgradeLink.getShipDetails().put(currentdetail.getId(), shipInfo);
                int nextId = currentdetail.getAfter_ship_id();
                if (nextId > 0) {
                    currentdetail = apiFeignClient.shipDetail(nextId); 
                } else {
                    currentdetail = null;
                }
            } else {
                upgradeLink.getUpgradeLinkIds().add(currentdetail.getId());
                currentdetail = null;
            }
        }
        log.info("get upgradeLink of ids = {}", upgradeLink.getUpgradeLinkIds());
        return upgradeLink;
    }
    
    @Override
    public InputStream download(String shipId, File rawDataFolder) {

        File gameDataImage = findGameDataImage(shipId, rawDataFolder);
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
    

    public File findGameDataImage(String shipId, File folder) {
        
        String fourBitId = shipId;
        int numAddZero = 4 - shipId.length();
        for (int i = 0; i < numAddZero; i++) {
            fourBitId = "0" + fourBitId;
        }

        
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

    @Override
    public File downloadOrFromCache(String fileId, File cacheFolder, File rawDataFolder) {
        return fileOperationDelegate.downloadOrFromCache(fileId, cacheFolder, rawDataFolder);
    }

}
