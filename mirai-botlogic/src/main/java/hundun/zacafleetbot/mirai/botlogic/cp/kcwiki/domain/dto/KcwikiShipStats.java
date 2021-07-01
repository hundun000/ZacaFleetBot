package hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.dto;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/21
 */
@Data
public class KcwikiShipStats {
    /*
     * {
        'id': 1,                                    // 舰娘ID
        'taik': [0, 1],                             // 耐久，[0]=初始值，[1]=最大值，下同
        'souk': [0, 1],                             // 装甲
        'houg': [0, 1],                             // 火力
        'raig': [0, 1],                             // 雷装
        'tyku': [0, 1],                             // 对空
        'luck': [0, 1],                             // 运
        'soku': 1,                                  // 速度 0=陆上基地, 5=低速, 10=高速
        'leng': 0,                                  // 射程 0=无, 1=短, 2=中, 3=长, 4=超长
        'slot_num': 2,                              // Slot 数量
        'max_eq': [0,0,0,0,0],                      // 舰载机搭载数
        'build_time': 15,                           // 建造时间，单位为分钟
        'broken': [1,1,1,1],                        // 解体资材（油弹钢铝）
        'pow_up': [1,1,1,1],                        // 近代化改修强化值
        'after_fuel': 100,                          // 改装钢材（？）
        'after_bull': 100,                          // 改装弹药
        'fuel_max': 15,                             // 消耗燃料
        'bull_max': 15                              // 消耗弹药             
    },
     */
    
    
    /**
     * 耐久，[0]=初始值，[1]=最大值，下同
     */
    int[] taik;
    /**
     * 装甲
     */
    int[] souk;
    /**
     * 火力
     */
    int[] houg;
    /**
     * 雷装
     */
    int[] raig;
    /**
     * 对空
     */
    int[] tyku;
    /**
     * 运
     */
    int[] luck;
    /**
     * 速度 0=陆上基地, 5=低速, 10=高速
     */
    int soku;
    /**
     * 射程 0=无, 1=短, 2=中, 3=长, 4=超长
     */
    int leng;
    /**
     * 回避
     */
    int kaih;
    /**
     * 对潜
     */
    int tais;
    /**
     * Slot 数量
     */
    int slot_num;
    /**
     * 舰载机搭载数
     */
    int[] max_eq;
    /**
     * 改装钢材（？）
     */
    int after_fuel;
    /**
     * 改装弹药
     */
    int after_bull;
    /**
     * 消耗燃料
     */
    int fuel_max;
    /**
     * 消耗弹药
     */
    int bull_max;
    /**
     * 解体资材（油弹钢铝）
     */
    int[] broken;
    /**
     * 近代化改修强化值
     */
    int[] pow_up;
    /**
     * 建造时间，单位为分钟
     */
    int build_time;
}
