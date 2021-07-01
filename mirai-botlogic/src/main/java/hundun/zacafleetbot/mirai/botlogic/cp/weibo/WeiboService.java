package hundun.zacafleetbot.mirai.botlogic.cp.weibo;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import gui.ava.html.image.generator.HtmlImageGenerator;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.db.WeiboCardCacheRepository;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.db.WeiboUserInfoCacheRepository;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboCardCache;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboUserInfoCache;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.feign.WeiboApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.feign.WeiboPictureApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.helper.file.FileOperationDelegate;
import hundun.zacafleetbot.mirai.botlogic.helper.file.IFileOperationDelegator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
@Component
public class WeiboService implements IFileOperationDelegator {
    ObjectMapper mapper = new ObjectMapper();
    
    
    FileOperationDelegate fileOperationDelegate;
    @Autowired
    WeiboApiFeignClient weiboApiFeignClient;
    @Autowired
    WeiboPictureApiFeignClient weiboPictureApiFeignClient;
    @Autowired
    WeiboUserInfoCacheRepository userInfoCacheRepository;
    @Autowired
    WeiboCardCacheRepository cardCacheRepository;

    
    //public static final String yjUid = "6279793937";
    //public static final String CHOSSHANLAND_UID = "6441489862";
    HtmlImageGenerator htmlImageGenerator = new HtmlImageGenerator();
    
    
    String API_TYPE_PARAM = "uid"; 
    
    public WeiboService() {
        this.fileOperationDelegate = new FileOperationDelegate(this);

    }
    
    
    
    private void updateBlogDetail(WeiboCardCache cardCache) {

        if (cardCache.getMblog_textDetail() == null) {
            
            log.info("updateBlogDetail get response.");
            try {
                //String responseString = weiboApiFeignClient.blogDetail(cardCache.getMblog_id());
                //JsonNode responseJson = mapper.readTree(responseString);
                JsonNode responseJson = weiboApiFeignClient.blogDetail(cardCache.getMblog_id());
                
                String longTextContent = responseJson.get("data").get("longTextContent").asText();
                
                String detailText = formatBlogDetail(longTextContent);
                cardCache.setMblog_textDetail(detailText);
                
                
                cardCacheRepository.save(cardCache);
                log.warn("updateBlogDetail success: {} ...", detailText.substring(0, Math.min(20, detailText.length())));
            } catch (Exception e) {
                log.warn("updateBlogDetail error:", e);
            }
        }

    }
    
    
    
    private static String formatBlogDetail(String text) {
        String detailText = text.replace("<br />", "\n");
        while (detailText.contains("<a") && detailText.contains("/a>")) {
            int start = detailText.indexOf("<a");
            int end = detailText.indexOf("/a>") + "/a>".length();
            
            detailText = detailText.substring(0, start) + detailText.substring(end, detailText.length());
        }
        return detailText;
    }
    
    public boolean updateUserInfoCache(String uid) {
        
        log.info("updateContainerid get response for uid = {}", uid);
        try {
            //String responseString = weiboApiFeignClient.get(uid, API_TYPE_PARAM, uid, null);
            //JsonNode responseJson = mapper.readTree(responseString);
            JsonNode responseJson = weiboApiFeignClient.get(uid, API_TYPE_PARAM, uid, null);
            JsonNode tabsNode = responseJson.get("data").get("tabsInfo").get("tabs");
            if (tabsNode.isArray()) {
                boolean updated = false;
                WeiboUserInfoCache userInfoCacahe;
                if (userInfoCacheRepository.existsById(uid)) {
                    userInfoCacahe = userInfoCacheRepository.findById(uid).get();
                } else {
                    userInfoCacahe = new WeiboUserInfoCache();
                    userInfoCacahe.setUid(uid);
                    String screen_name = responseJson.get("data").get("userInfo").get("screen_name").asText();
                    userInfoCacahe.setScreen_name(screen_name);
                    updated = true;
                }
                
                for (final JsonNode tabNode : tabsNode) {
                    if (tabNode.get("tabKey").asText().equals("weibo")) {
                        String newContainerid = tabNode.get("containerid").asText();
                        if (userInfoCacahe.getWeibo_containerid() == null || !userInfoCacahe.getWeibo_containerid().equals(newContainerid)) {
                            userInfoCacahe.setWeibo_containerid(newContainerid);
                            updated = true;
                        }
                        break;
                    }
                }
                
                if (updated) {
                    userInfoCacheRepository.save(userInfoCacahe);
                    log.info("userInfoCacahe updated: {}", userInfoCacahe);
                } else {
                    log.info("userInfoCacahe is up-to-date: {}", uid);
                }
                return true;
            } else {
                log.warn("tabsNode not array, responseJson = {}", responseJson);
            }
            
        } catch (Exception e) {
            log.warn("updateContainerid :", e);
        }
        return false;
    }
    
    public String getFirstBlogInfo(String uid) {
        List<WeiboCardCache> cardCaches = cardCacheRepository.findTop5ByUidOrderByMblogCreatedDateTimeDesc(uid);
//        Collections.sort(cardCaches, new Comparator<WeiboCardCache>() {
//            public int compare(WeiboCardCache o1, WeiboCardCache o2) {
//                if (o1.getMblogCreatedDateTime() == null || o2.getMblogCreatedDateTime() == null)
//                  return 0;
//                return -1 * o1.getMblogCreatedDateTime().compareTo(o2.getMblogCreatedDateTime());
//            }
//          });
        
        String text = null;
        if (!cardCaches.isEmpty()) {
//            if (cardCache.getMblog_textDetail() == null) {
//                updateBlogDetail(cardCache);
//            }
            text = "来自：" + cardCaches.get(0).getScreenName() + "，最新的饼的时间是：" + cardCaches.get(0).getMblogCreatedDateTime().toString();
        }
        return text;
    }
    
    // Sat Apr 10 11:16:34 +0800 2021
    // 
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
    
    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(dateTimeFormatter.format(now));  
        ZonedDateTime localDateTime = ZonedDateTime.parse("Sat Apr 10 11:16:34 +0800 2021", dateTimeFormatter);
        System.out.println(localDateTime.toString());
    }
    
    @AllArgsConstructor
    @Data
    public class WeiboCardCacheAndImage {
        WeiboCardCache weiboCardCache;
        @ToString.Exclude
        File image;
    }
    
    public List<WeiboCardCacheAndImage> updateAndGetTopBlog(String uid, File cacheFolder) {
        List<WeiboCardCacheAndImage> newBlogs = new ArrayList<>(0);
        WeiboUserInfoCache userInfoCacahe;
        if (!userInfoCacheRepository.existsById(uid)) {
            boolean success = updateUserInfoCache(uid);
            if (!success) {
                return newBlogs;
            }
        }
        userInfoCacahe = userInfoCacheRepository.findById(uid).get();
        
        try {
            //String responseString = weiboApiFeignClient.get(uid, API_TYPE_PARAM, uid, userInfoCacahe.getWeibo_containerid());
            //JsonNode responseJson = mapper.readTree(responseString);
            JsonNode responseJson = weiboApiFeignClient.get(uid, API_TYPE_PARAM, uid, userInfoCacahe.getWeibo_containerid());
            JsonNode cardsNode = responseJson.get("data").get("cards");
            
            for (final JsonNode cardNode : cardsNode) {
                try {
                    String itemid = cardNode.get("itemid").asText();
                    JsonNode mblog = cardNode.get("mblog");
                    WeiboCardCache cardCache;
                    
                    if (!cardCacheRepository.existsById(itemid)) {
                        cardCache = new WeiboCardCache(); 
                        cardCache.setItemid(itemid);
                        cardCache.setUid(uid);
                        
                        String mblog_id = mblog.get("id").asText();
                        String mblog_text = mblog.get("text").asText();
                        String mblog_created_at = mblog.get("created_at").asText();
                        ZonedDateTime utcZoned = ZonedDateTime.parse(mblog_created_at, dateTimeFormatter);
                        LocalDateTime localDateTime = utcZoned.toLocalDateTime();
                        List<String> picsLargeUrls = new ArrayList<>();
                        JsonNode picsNode = mblog.get("pics");
                        if (picsNode != null && picsNode.isArray()) {
                            for (final JsonNode picNode : picsNode) {
                                String largUrl = picNode.get("large").get("url").asText();
                                picsLargeUrls.add(largUrl);
                            }
                        }
                        
                        
                        cardCache.setMblogCreatedDateTime(localDateTime);
                        cardCache.setMblog_text(mblog_text);
                        cardCache.setMblog_id(mblog_id);
                        cardCache.setScreenName(userInfoCacahe.getScreen_name());
                        cardCache.setPicsLargeUrls(picsLargeUrls);
                        updateBlogDetail(cardCache);
                        
                        cardCacheRepository.save(cardCache);
                        
                        
                        
                        log.info("update cardCache: {}", itemid);
                    } else {
                        cardCache = cardCacheRepository.findById(itemid).get();
                        
                    }
                    File imageFile = checkSingleImage(cardCache, cacheFolder);
                    WeiboCardCacheAndImage cardCacheAndImage = new WeiboCardCacheAndImage(cardCache, imageFile);
                    
                    
                    newBlogs.add(cardCacheAndImage);
                } catch (Exception e) {
                    log.warn("itera card error {} by : {}", e.getMessage(), cardNode);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.warn("updateBlog: ", e);
        }
        return newBlogs;
        
    }



    private File checkSingleImage(WeiboCardCache cardCache, File cacheFolder) {
        if (cardCache.getPicsLargeUrls() != null && cardCache.getPicsLargeUrls().size() == 1) {
            int lastSlash = cardCache.getPicsLargeUrls().get(0).lastIndexOf("/");
            String id = cardCache.getPicsLargeUrls().get(0).substring(lastSlash + 1);
            File file = this.downloadOrFromCache(id, cacheFolder, null);
            return file;
        } else {
            return null;
        }
    }



    @Override
    public InputStream download(String fileId, File cacheFolder) {
        try {
            final Response response = weiboPictureApiFeignClient.pictures(fileId);
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
        return "weibo";
    }



    @Override
    public File downloadOrFromCache(String fileId, File cacheFolder, File rawDataFolder) {
        return fileOperationDelegate.downloadOrFromCache(fileId, cacheFolder, rawDataFolder);
    }
    
}
