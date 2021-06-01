package com.hundun.mirai.bot.cp.penguin.db;

import org.bson.conversions.Bson;

import com.hundun.mirai.bot.cp.penguin.domain.Item;
import com.hundun.mirai.bot.db.BaseRepositoryImplement;
import com.hundun.mirai.bot.db.CollectionSettings;
import com.mongodb.client.model.Filters;


/**
 * @author hundun
 * Created on 2021/05/31
 */
public class ItemRepositoryImplement extends BaseRepositoryImplement<Item> implements ItemRepository {

    public ItemRepositoryImplement(CollectionSettings<Item> mongoSettings) {
        super(mongoSettings);
        
    }

    @Override
    public Item findTopByName(String name) {
        Bson filter = Filters.eq("name", name);
        return collection.find(filter).first();
    }

    


}
