package hundun.zacafleetbot.mirai.botlogic.cp.weibo;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

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
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboViewFormat;
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

        if (cardCache.getBlogTextDetail() == null) {
            
            log.info("updateBlogDetail get response.");
            try {
                //String responseString = weiboApiFeignClient.blogDetail(cardCache.getMblog_id());
                //JsonNode responseJson = mapper.readTree(responseString);
                JsonNode responseJson = weiboApiFeignClient.blogDetail(cardCache.getBlogId());
                
                String longTextContent = responseJson.get("data").get("longTextContent").asText();
                
                String detailText = formatBlogDetail(longTextContent);
                cardCache.setBlogTextDetail(detailText);
                
                
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
    
    public WeiboUserInfoCache updateAndGetUserInfoCache(String uid, boolean forceUpdate) {
        
        if (userInfoCacheRepository.existsById(uid) && !forceUpdate) {
            return userInfoCacheRepository.findById(uid).get();
        }
        
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
                    userInfoCacahe.setScreenName(screen_name);
                    updated = true;
                }
                
                for (final JsonNode tabNode : tabsNode) {
                    if (tabNode.get("tabKey").asText().equals("weibo")) {
                        String newContainerid = tabNode.get("containerid").asText();
                        if (userInfoCacahe.getWeiboContainerid() == null || !userInfoCacahe.getWeiboContainerid().equals(newContainerid)) {
                            userInfoCacahe.setWeiboContainerid(newContainerid);
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
                return userInfoCacahe;
            } else {
                log.warn("tabsNode not array, responseJson = {}", responseJson);
            }
            
        } catch (Exception e) {
            log.warn("updateContainerid :", e);
        }
        return null;
    }
    
    public String getUidByUserName(String userName) {
        WeiboUserInfoCache userInfoCacahe = userInfoCacheRepository.findOneByScreenName(userName);
        if (userInfoCacahe == null) {
            return null;
        } 
        return userInfoCacahe.getUid();
    }
    
    public WeiboCardCacheAndImage getFirstBlogByUserName(String userName, int index, File cacheFolder, WeiboViewFormat format) {
        WeiboUserInfoCache userInfoCacahe = userInfoCacheRepository.findOneByScreenName(userName);
        if (userInfoCacahe == null) {
            return null;
        } 
        List<WeiboCardCache> cardCaches = cardCacheRepository.findTop5ByUidOrderByBlogCreatedDateTimeDesc(userInfoCacahe.getUid());
        if (cardCaches.isEmpty() || index >= cardCaches.size()) {
            return null;
        }
        WeiboCardCache cardCache = cardCaches.get(index);
        WeiboCardCacheAndImage cardCacheAndImage = handleImageFormat(cardCache, cacheFolder, format);
        return cardCacheAndImage;
    }
    
    public String getFirstBlogInfo(String uid) {
        List<WeiboCardCache> cardCaches = cardCacheRepository.findTop5ByUidOrderByBlogCreatedDateTimeDesc(uid);
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
            text = "来自：" + cardCaches.get(0).getScreenName() + "，最新的饼的时间是：" + cardCaches.get(0).getBlogCreatedDateTime().toString();
        }
        return text;
    }
    
//    // Sat Apr 10 11:16:34 +0800 2021
//    // 
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
//    
//    public static void main(String[] args) {
//        ZonedDateTime now = ZonedDateTime.now();
//        System.out.println(dateTimeFormatter.format(now));  
//        ZonedDateTime localDateTime = ZonedDateTime.parse("Sat Apr 10 11:16:34 +0800 2021", dateTimeFormatter);
//        System.out.println(localDateTime.toString());
//    }
    
    @AllArgsConstructor
    @Data
    public class WeiboCardCacheAndImage {
        WeiboCardCache weiboCardCache;
        @ToString.Exclude
        List<File> images;
        @ToString.Exclude
        List<String> imageUrls;
    }
    
    public List<WeiboCardCacheAndImage> updateAndGetTopBlog(String uid, File cacheFolder, WeiboViewFormat format) {
        List<WeiboCardCacheAndImage> newBlogs = new ArrayList<>(0);
        WeiboUserInfoCache userInfoCacahe = updateAndGetUserInfoCache(uid, false);
        
        if (userInfoCacahe == null) {
            return newBlogs;
        }

        try {
            JsonNode responseJson = weiboApiFeignClient.get(uid, API_TYPE_PARAM, uid, userInfoCacahe.getWeiboContainerid());
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
                        
                        
                        cardCache.setBlogCreatedDateTime(localDateTime);
                        cardCache.setBlogText(mblog_text);
                        cardCache.setBlogId(mblog_id);
                        cardCache.setScreenName(userInfoCacahe.getScreenName());
                        cardCache.setPicsLargeUrls(picsLargeUrls);
                        updateBlogDetail(cardCache);
                        
                        cardCacheRepository.save(cardCache);
                        
                        
                        
                        log.info("update cardCache: {}", itemid);
                    } else {
                        cardCache = cardCacheRepository.findById(itemid).get();
                        
                    }
                    
                    
                    
                    
                    WeiboCardCacheAndImage cardCacheAndImage = handleImageFormat(cardCache, cacheFolder, format);
                    
                    
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



    private WeiboCardCacheAndImage handleImageFormat(WeiboCardCache cardCache, File cacheFolder, WeiboViewFormat format) {
        
        List<File> files = new ArrayList<File>();
        List<String> urls = new ArrayList<String>();
        if (cardCache.getPicsLargeUrls() != null) {
            urls.addAll(cardCache.getPicsLargeUrls());
        }
        
        switch (format) {
            case FIRST_IMAGE:
                File imageFile = removeUrlToImage(urls, cacheFolder, 0);
                if (imageFile != null) {
                    files.add(imageFile);
                }
                break;
            case ALL_IMAGE:
                files = removeUrlsToImages(urls, cacheFolder);
                break;
            case NO_IMAGE:
            default:
                files = new ArrayList<>(0);
        }
        
        
        return new WeiboCardCacheAndImage(cardCache, files, urls);
    }

    private List<File> removeUrlsToImages(List<String> urls, File cacheFolder) {
        List<File> files = new ArrayList<>(urls.size());

        while (!urls.isEmpty()) {
            File file = removeUrlToImage(urls, cacheFolder, 0);
            files.add(file);
        }

        return files;
    }

    private File removeUrlToImage(List<String> urls, File cacheFolder, int index) {
        if (urls.size() > index) {
            int lastSlash = urls.get(index).lastIndexOf("/");
            String id = urls.get(index).substring(lastSlash + 1);
            File file = this.downloadOrFromCache(id, cacheFolder, null);
            urls.remove(index);
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
