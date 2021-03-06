package com.italker.factory.data.message;


import com.italker.factory.data.callback.DbDataSource;
import com.italker.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository
 * 关注的对象是Message表
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
