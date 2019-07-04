package utils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;

/*
    详见：https://blog.csdn.net/hao_kkkkk/article/details/51198611
 */
public class CouchBaseUtils {
    private static final Logger logger = Logger.getLogger(CouchBaseUtils.class);
    private static String couchBasePath;
    private static Cluster cluster;
    private static List<String> couchBasePathList = new ArrayList<String>();
    private static Map<String, Bucket> bucketMap = new HashMap<String, Bucket>();
    private static Properties properties = PropertiesUtils.loadProps("");

    static{
        couchBasePath = PropertiesUtils.getString(properties,"couchBase.Server.path");
    }

    //初始化化缓存相关配置，外部必须显示调用此方法
    public static void init() {
        cluster = CouchbaseCluster.create(getCouchBasePathList(couchBasePath));
        bucketMap.put(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_product")),openBucket(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_product")),getBucketPwd(PropertiesUtils.getString(properties,"couchBase.website_product"))));
        bucketMap.put(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_company")),openBucket(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_company")),getBucketPwd(PropertiesUtils.getString(properties,"couchBase.website_company"))));
        bucketMap.put(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_product_group")),openBucket(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_product_group")),getBucketPwd(PropertiesUtils.getString(properties,"couchBase.website_product_group"))));
        bucketMap.put(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_fundmanager")),openBucket(getBucketName(PropertiesUtils.getString(properties,"couchBase.website_fundmanager")),getBucketPwd(PropertiesUtils.getString(properties,"couchBase.website_fundmanager"))));
        logger.info("缓存服务器连接成功...");
    }

    /**
     *  打开桶
     * @param bucketName 桶名
     * @param password 密码
     * @return
     */
    private static Bucket openBucket(String bucketName,String password) {
        if(StringUtils.isEmpty(password)) {
            return cluster.openBucket(bucketName,30, TimeUnit.SECONDS);
        } else {
            return cluster.openBucket(bucketName,password,30,TimeUnit.SECONDS);
        }
    }

    public static List<String> getCouchBasePathList(String path){
        if(path.contains(",")){
            String[] arrayOne = path.split("\\,");
            for(int i = 0;i < arrayOne.length;i++){
                couchBasePathList.add(arrayOne[i]);
            }
        }else{
            couchBasePathList.add(path);
        }
        return couchBasePathList;
    }


    public static List<Map<String,Object>> queryCouchBase(String sqlKey,String bucketName) {
        Bucket bucket = null;
            if (bucketName.contains(",")) {
                String[] arrayTwo = bucketName.split("\\,");
                if (arrayTwo.length != 2) {
                    logger.warn("配置文件有误！");
                }
                bucket = bucketMap.get(arrayTwo[0]);
            } else {
                bucket = bucketMap.get(bucketName);
            }
            List<Map<String, Object>> mapList = getMapList(sqlKey, bucket);
            return mapList;
        }

    public static String getBucketName(String temp){
        if(temp.contains(",")){
            String[] arrayTwo = temp.split("\\,");
            if(arrayTwo.length != 2){
                logger.warn("配置文件有误！");
            }
            return arrayTwo[0];
        }else{
            return temp;
        }
    }

    public static String getBucketPwd(String temp){
        if(temp.contains(",")){
            String[] arrayTwo = temp.split("\\,");
            if(arrayTwo.length != 2){
                logger.warn("配置文件有误！");
            }
            return arrayTwo[1];
        }else{
            return temp;
        }
    }

    //下边这段代码是根据sql语句从桶中查询信息，并转换成mapList返回。
    public static List<Map<String,Object>> getMapList(String sqlKey,Bucket bucket){
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        N1qlQueryResult result;
        result = bucket.query(N1qlQuery.simple(sqlKey));
        Iterator<N1qlQueryRow> iterator = result.iterator();
        while (iterator.hasNext()) {
            N1qlQueryRow n1qlQueryRow = iterator.next();
            JsonObject jsonObject = n1qlQueryRow.value();
            Set<String> set = jsonObject.getNames();
            Iterator<String> iterator1 = set.iterator();
            Map<String,Object> map = new HashMap<String, Object>();
            while (iterator1.hasNext()) {
                String key = iterator1.next();
                Object object = jsonObject.get(key);
                if(object instanceof JsonArray) {
                    JsonArray value = (JsonArray) object;
                    List<Object> list1 = new ArrayList<Object>();
                    for(int i=0;i<value.size();i++) {
                        Object obj = value.get(i);
                        if(obj instanceof JsonObject) {
                            JsonObject jsonObject1 = (JsonObject) obj;
                            list1.add(jsonObject1.toMap());
                        } else {
                            list1.add(value.getString(i));
                        }
                    }
                    map.put(key,list1);
                } else if(object instanceof JsonObject) {
                    JsonObject value = (JsonObject) object;
                    Map map1 = value.toMap();
                    map.put(key,map1);
                } else {
                    map.put(key,object);
                }
            }
            mapList.add(map);
        }
        return mapList;
    }
}
