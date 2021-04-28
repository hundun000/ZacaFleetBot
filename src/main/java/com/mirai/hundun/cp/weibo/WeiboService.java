package com.mirai.hundun.cp.weibo;

import java.io.IOException;
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

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
@Service
public class WeiboService {
    ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    WeiboApiService weiboApiService;
    
    @Autowired
    WeiboUserInfoCacheRepository userInfoCacheRepository;
    
    @Autowired
    WeiboCardCacheRepository cardCacheRepository;
    
    
    
    public static final String yjUid = "6279793937";
    public static final String CHOSSHANLAND_UID = "6441489862";
    
    
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
                log.warn("updateBlogDetail success: {}", detailText);
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
    
    public void updateUserInfoCache(String uid) {
        
        String responseString = weiboApiService.get(uid, API_TYPE_PARAM, uid, null);
        log.info("updateContainerid get response.");
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
            } else {
                log.warn("tabsNode not array: {}", tabsNode);
            }
            
        } catch (Exception e) {
            log.warn("updateContainerid mapper.readTree cannot read: {}", responseString);
        }
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
    
    public List<WeiboCardCache> updateBlog(String uid) {
        List<WeiboCardCache> newBlogs = new ArrayList<>(0);
        WeiboUserInfoCache userInfoCacahe;
        if (!userInfoCacheRepository.existsById(uid)) {
            updateUserInfoCache(uid);
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
                        cardCache.setMblogCreatedDateTime(localDateTime);
                        cardCache.setMblog_text(mblog_text);
                        cardCache.setMblog_id(mblog_id);
                        cardCache.setScreenName(userInfoCacahe.getScreen_name());
                        updateBlogDetail(cardCache);
                        
                        cardCacheRepository.save(cardCache);
                        newBlogs.add(cardCache);
                        log.info("update cardCache: {}", itemid);
                    }
                } catch (Exception e) {
                    log.warn("itera card error: {}", cardNode);
                }
            }
        } catch (Exception e) {
            log.warn("updateBlog mapper.readTree cannot read: {}", responseString);
        }
        return newBlogs;
        
    }
    
}
