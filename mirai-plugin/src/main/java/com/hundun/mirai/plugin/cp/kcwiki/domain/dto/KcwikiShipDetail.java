package com.hundun.mirai.plugin.cp.kcwiki.domain.dto;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/21
 */
@Data
public class KcwikiShipDetail {
    
    /*
     * {
    "id": 9,
    "sort_no": 11,
    "name": "吹雪",
    "yomi": "ふぶき",
    "stype": 2,
    "ctype": 12,
    "cnum": 1,
    "backs": 3,
    "after_lv": 20,
    "after_ship_id": 201,
    "get_mes": "はじめまして吹雪です。<br>よろしくお願い致します。",
    "voice_f": 0,
    "filename": "gyckjmemgqoe",
    "file_version": [
        "34",
        "20",
        "423"
    ],
    "book_table_id": [
        9,
        5119,
        5393
    ],
    "book_sinfo": "ワシントン条約制限下で設計された、世界中を驚愕さ<br>せたクラスを超えた特型駆逐艦の１番艦、吹雪です。<br>私たちは、後の艦隊型駆逐艦のベースとなりました。<br>はいっ、頑張ります！",
    "stats": {
        ……
    },
    "graph": {
        ……
    },
    "wiki_id": "011",
    "chinese_name": "吹雪",
    "can_drop": true,
    "can_construct": true,
    "stype_name": "駆逐艦",
    "stype_name_chinese": "驱逐舰",
    "swf": "/kcs/resources/swf/ships/gyckjmemgqoe.swf"
}
     */
    
    
    
    int id;
    String name;
    String yomi;
    int sort_no;
    int after_lv;
    int after_ship_id;
    String wiki_id;
    String chinese_name;
    String stype_name_chinese;
    KcwikiShipStats stats;
}
