package com.hundun.mirai.plugin.cp.penguin;


import java.util.List;

import com.hundun.mirai.plugin.cp.penguin.domain.Item;

/**
 * @author hundun
 * Created on 2021/04/26
 */
public interface ItemRepository {
    Item findTopByName(String name);
    //List<Item> findAllByNameLike(String name);

    void deleteAll();

    void saveAll(List<Item> items);

    Item findById(String itemId);
}
