package utils.common;

import com.mongodb.DBObject;

/**
 * MongoDB更新操作接口定义
 *
 * @author: alex
 * @time: 14-1-21 下午5:25
 * @version: 1.0
 */
public interface UpdateCallback {
    DBObject doCallback(DBObject valueDBObject);
}
