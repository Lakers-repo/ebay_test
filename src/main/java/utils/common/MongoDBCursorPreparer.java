package utils.common;

import com.mongodb.DBCursor;

/**
 * 查询转换接口定义
 *
 * @author: alex
 * @time: 14-1-21 下午4:55
 * @version: 1.0
 */
public interface MongoDBCursorPreparer {
    DBCursor prepare(DBCursor cursor);
}
