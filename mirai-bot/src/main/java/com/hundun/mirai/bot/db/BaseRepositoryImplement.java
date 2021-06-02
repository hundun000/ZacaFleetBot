package com.hundun.mirai.bot.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.conversions.Bson;

import com.hundun.mirai.bot.function.reminder.ReminderTask;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;


/**
 * @author hundun
 * Created on 2021/05/31
 * @param <T>
 */
public abstract class BaseRepositoryImplement<T> {
    protected final MongoDatabase database;
    protected final MongoCollection<T> collection;
    protected final String idFieldName;
    
    private final IdGetter<T> idGetter;
    private final IdSetter<T> idSetter;
    
    @FunctionalInterface
    public interface IdGetter<T> {
        String get(T item);
    }
    
    @FunctionalInterface
    public interface IdSetter<T> {
        void set(T item, String id);
    }
    
    public BaseRepositoryImplement(CollectionSettings<T> mongoSettings) {
        this.database = mongoSettings.getDatabase();
        this.collection = database.getCollection(mongoSettings.getCollectionName(), mongoSettings.getType());
        this.idFieldName = mongoSettings.getIdFieldName();
        this.idGetter = mongoSettings.getIdGetter();
        this.idSetter = mongoSettings.getIdSetter();
    }
    
    
    public void deleteAll() {
        
    }

    public void delete(T item) {
        String id = idGetter.get(item);
        if (id != null) {
            deleteById(id);
        }
    }
    
    public void deleteById(String id) {
        Bson filter = Filters.eq(idFieldName, id);
        collection.deleteOne(filter);
    }

    public void save(T item) {
        String id = idGetter.get(item);
        
        if (id == null) {
            id = UUID.randomUUID().toString();
            idSetter.set(item, id);
            collection.insertOne(item);
        } else {
            Bson filter = Filters.eq(idFieldName, id);
            collection.replaceOne(filter, item, new ReplaceOptions().upsert(true));
        }
    }
    
    public void saveAll(List<T> items) {
        for (T item: items) {
            this.save(item);
        }
    }

    
    public T findById(String itemId) {
        Bson filter = Filters.eq(idFieldName, itemId);
        return collection.find(filter).first();
    }
    
    public List<T> findAll() {
        Bson filter = new BasicDBObject();
        return findAllByFilter(filter, null);
    }

    public List<T> findAllByFilter(Bson filter, Integer topLimit) {
        return findAllByFilter(filter, null, topLimit);
    }
    
    public List<T> findAllByFilter(Bson filter, Bson sorter, Integer topLimit) {
        List<T> result = new ArrayList<>();
        FindIterable<T> findIterable = collection.find(filter);
        
        if (sorter != null) {
            findIterable = findIterable.sort(sorter);
        }
        if (topLimit != null) {
            findIterable = findIterable.limit(topLimit);
        }
        MongoCursor<T> cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            result.add(cursor.next());
        }
        return result;
    }
    
    public boolean existsById(String itemId) {
        Bson filter = Filters.eq(idFieldName, itemId);
        return collection.countDocuments(filter) > 0;
    }
    
}
