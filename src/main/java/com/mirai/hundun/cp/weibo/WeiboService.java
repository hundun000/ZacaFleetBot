package com.mirai.hundun.cp.weibo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.cp.weibo.domain.WeiboUserInfoCache;
import com.mirai.hundun.cp.weibo.feign.WeiboApiService;
import com.mirai.hundun.cp.weibo.feign.WeiboPictureApiService;
import com.mirai.hundun.service.file.FileService;
import com.mirai.hundun.service.file.IFileProvider;

import feign.Response;
import gui.ava.html.image.generator.HtmlImageGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
@Service
public class WeiboService implements IFileProvider {
    ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    FileService fileService;
    
    @Autowired
    WeiboApiService weiboApiService;
    
    @Autowired
    WeiboPictureApiService weiboPictureApiService;
    
    @Autowired
    WeiboUserInfoCacheRepository userInfoCacheRepository;
    
    @Autowired
    WeiboCardCacheRepository cardCacheRepository;

    
    //public static final String yjUid = "6279793937";
    //public static final String CHOSSHANLAND_UID = "6441489862";
    HtmlImageGenerator htmlImageGenerator = new HtmlImageGenerator();
    
    
    String API_TYPE_PARAM = "uid"; 
    

    
    private void updateBlogDetail(WeiboCardCache cardCache) {

        if (cardCache.getMblog_textDetail() == null) {
            String responseString = weiboApiService.blogDetail(cardCache.getMblog_id());
            log.info("updateBlogDetail get response.");
            try {
                JsonNode responseJson = mapper.readTree(responseString);
                String longTextContent = responseJson.at("/data/longTextContent").asText();
                
                String detailText = formatBlogDetail(longTextContent);
                cardCache.setMblog_textDetail(detailText);
                
                
                cardCacheRepository.save(cardCache);
                log.warn("updateBlogDetail success: {} ...", detailText.substring(0, Math.min(20, detailText.length())));
            } catch (Exception e) {
                log.warn("updateBlogDetail error: {}", responseString);
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
        
        String responseString = weiboApiService.get(uid, API_TYPE_PARAM, uid, null);
        log.info("updateContainerid get response for uid = {}", uid);
        try {
            JsonNode responseJson = mapper.readTree(responseString);
            JsonNode tabsNode = responseJson.at("/data/tabsInfo/tabs");
            if (tabsNode.isArray()) {
                boolean updated = false;
                WeiboUserInfoCache userInfoCacahe;
                if (userInfoCacheRepository.existsById(uid)) {
                    userInfoCacahe = userInfoCacheRepository.findById(uid).get();
                } else {
                    userInfoCacahe = new WeiboUserInfoCache();
                    userInfoCacahe.setId(uid);
                    String screen_name = responseJson.at("/data/userInfo/screen_name").asText();
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
            log.warn("updateContainerid mapper.readTree cannot read: {}", responseString);
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
    
    public List<WeiboCardCache> updateAndGetTopBlog(String uid) {
        List<WeiboCardCache> newBlogs = new ArrayList<>(0);
        WeiboUserInfoCache userInfoCacahe;
        if (!userInfoCacheRepository.existsById(uid)) {
            boolean success = updateUserInfoCache(uid);
            if (!success) {
                return newBlogs;
            }
        }
        userInfoCacahe = userInfoCacheRepository.findById(uid).get();
        
        String responseString = weiboApiService.get(uid, API_TYPE_PARAM, uid, userInfoCacahe.getWeibo_containerid());
        try {
            JsonNode responseJson = mapper.readTree(responseString);
            JsonNode cardsNode = responseJson.at("/data/cards");
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
                        checkSingleImage(cardCache);
                        cardCacheRepository.save(cardCache);
                        
                        log.info("update cardCache: {}", itemid);
                    } else {
                        cardCache = cardCacheRepository.findById(itemid).get();
                    }
                    newBlogs.add(cardCache);
                } catch (Exception e) {
                    log.warn("itera card error: {}", cardNode);
                }
            }
        } catch (Exception e) {
            log.warn("updateBlog mapper.readTree cannot read: {}", responseString);
        }
        return newBlogs;
        
    }



    private void checkSingleImage(WeiboCardCache cardCache) {
        if (cardCache.getPicsLargeUrls() != null && cardCache.getPicsLargeUrls().size() == 1) {
            int lastSlash = cardCache.getPicsLargeUrls().get(0).lastIndexOf("/");
            String id = cardCache.getPicsLargeUrls().get(0).substring(lastSlash + 1);
            File file = fileService.downloadOrFromCache(id, this);
            cardCache.setSinglePicture(file);
        }
    }



    @Override
    public InputStream download(String fileId) {
        try {
            final Response response = weiboPictureApiService.pictures(fileId);
            final Response.Body body = response.body();
            final InputStream inputStream = body.asInputStream();
            return inputStream;
        } catch (Exception e) {
            log.info("download image faild {} {}", fileId, e);
            return null;
        }
    }



    @Override
    public String getCacheSubFolerName() {
        return "weibo";
    }
    
}
