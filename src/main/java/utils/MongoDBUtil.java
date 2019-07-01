package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class MongoDBUtil {
    /**
     * mongodb数据库地址	192.168.1.188
     */
    private static String host = "127.0.0.1";
    /**
     * 端口号
     */
    private static Integer port = 27017;

    private static MongoClient mongoClient = null;

    static {
        mongoClient = new MongoClient(host, port);
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(300); // 连接池设置为300个连接,默认为100
        builder.connectTimeout(15000);//连接超时，推荐>3000毫秒
        builder.maxWaitTime(5000);//最大等待时间
        builder.socketTimeout(0);//套接字超时时间，0无限制
        builder.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        builder.build();
    }

    /**
     * 方法: findOneById <br>
     * 描述: 通过id查询单条数据 <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-22 上午11:31:46
     *
     * @param dbName 数据库名称
     * @param coll   集合名称
     * @param id     id
     * @return Document
     */
    public static Document findOneById(String dbName, String coll, String id) {
        MongoCollection<Document> collection = getColls(dbName, coll);
        Document document = new Document();
        document.put("_id", new ObjectId(id));
        FindIterable<Document> find = collection.find(document);
        MongoCursor<Document> iterator = find.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    /**
     * 方法: add <br>
     * 描述: 保存单条数据 <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-21 下午3:06:43
     *
     * @param dbName   数据库名字
     * @param coll     集合名字
     * @param document 保存的document
     */
    public static void add(String dbName, String coll, Document document) {
        MongoCollection<Document> collection = getColls(dbName, coll);
        collection.insertOne(document);
    }

    /**
     * 方法: getDB <br>
     * 描述: 获取collection <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-21 下午3:10:07
     *
     * @param dbName 数据库名
     * @param coll   集合名
     * @return MongoCollection<Document>
     */
    private static MongoCollection<Document> getColls(String dbName, String coll) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(coll);
        return collection;
    }

    /**
     * 方法: delByIds <br>
     * 描述: 根据id批量删除 <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-21 下午3:09:04
     *
     * @param dbName
     * @param coll
     * @param ids
     */
    public static long delByIds(String dbName, String coll, String... ids) {
        MongoCollection<Document> colls = getColls(dbName, coll);
        Document document = new Document();
        Document document2 = new Document();
        ArrayList<ObjectId> arrayList = new ArrayList<ObjectId>();
        for (int i = 0; i < ids.length; i++) {
            arrayList.add(new ObjectId(ids[i]));
        }
        document2.put("$in", arrayList);
        document.put("_id", document2);
        DeleteResult deleteMany = colls.deleteMany(document);
        long deletedCount = deleteMany.getDeletedCount();
        return deletedCount;
    }

    /**
     * 方法: updateById <br>
     * 描述: 根据id修改 <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-21 下午3:21:26
     *
     * @param dbName   数据库名称
     * @param coll     集合名称
     * @param id       需要修改的数据id
     * @param document 需要修改的内容
     * @return
     */
    public static UpdateResult updateById(String dbName, String coll, String id, Document document) {
        MongoCollection<Document> colls = getColls(dbName, coll);
        Document where = new Document();
        where.put("_id", new ObjectId(id));
        UpdateResult updateMany = colls.updateMany(where, document);
        return updateMany;
    }

    /**
     * 方法: find <br>
     * 描述: 查询带分页排序 <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-21 下午3:30:15
     *
     * @param dbName   数据库名字
     * @param coll     集合名字
     * @param where    搜索条件
     * @param page     页数
     * @param rows     每页条数
     * @param isSort   是否排序
     * @param sortName 排序字段（如果isSort为false的时候 ,该字段不生效）
     * @param sort     排序方式（1：正序 -1：倒序【如果isSort为false的时候 ,该字段不生效】）
     * @return MongoCursor<Document>
     */
    public static MongoCursor<Document> find(String dbName, String coll, Document where, Integer page, Integer rows, boolean isSort, String sortName, Integer sort) {
        MongoCollection<Document> colls = getColls(dbName, coll);
        FindIterable<Document> find = null;
        //搜索条件
        if (where != null && !where.isEmpty()) {
            find = colls.find(where);
        } else {
            find = colls.find();
        }
        //排序
        if (isSort) {
            Document sortFlag = new Document();
            sortFlag.put(sortName, sort);
            find.sort(sortFlag);
        }
        FindIterable<Document> limit = find.skip((page - 1) * rows).limit(rows);
        MongoCursor<Document> iterator = limit.iterator();
        return iterator;
    }

    /**
     * 方法: getCount <br>
     * 描述: 查询总数 <br>
     * 作者: Teacher song<br>
     * 时间: 2017-4-21 下午3:35:49
     *
     * @param dbName 数据库名称
     * @param coll   集合名称
     * @param where  筛选条件
     * @return Integer
     */
    public static Integer getCount(String dbName, String coll, Document where) {
        MongoCollection<Document> colls = getColls(dbName, coll);
        if (where != null && !where.isEmpty()) {
            return (int) colls.count(where);
        } else {
            return (int) colls.count();
        }
    }
}
