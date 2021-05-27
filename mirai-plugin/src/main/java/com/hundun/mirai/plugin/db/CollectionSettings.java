package com.hundun.mirai.plugin.db;

import com.hundun.mirai.plugin.db.BaseRepositoryImplement.IdGetter;
import com.hundun.mirai.plugin.db.BaseRepositoryImplement.IdSetter;
import com.mongodb.client.MongoDatabase;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/31
 * @param <T>
 */
@Data
@AllArgsConstructor
public class CollectionSettings<T> {
    MongoDatabase database;
    String collectionName;
    Class<T> type;
    String idFieldName;
    IdGetter<T> idGetter;
    IdSetter<T> idSetter;
}
