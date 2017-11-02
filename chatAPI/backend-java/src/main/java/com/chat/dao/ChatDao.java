package com.chat.dao;

import com.chat.model.Message;
import com.chat.model.User;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by asazawal on 10/21/17.
 * DAO Layer.Assumes MYSQL underlying DB.
 * Perform API specific database operations.
 */

@RegisterMapper(MessageMapper.class)
public abstract class ChatDao {


    /**
     * Inserts new user after hashing Password.
     * @param user
     * @return {@code Integer}
     */
    @SqlUpdate("INSERT INTO USERS(USERNAME, PASSWD) values(:username, md5(:passwd))")
    public abstract int addUser(@BindBean final User user);

    /**
     * Adds message row.
     * Should NOT be called directly cause of transactional nature
     * Use  addMessage instead.
     * @param message
     * @return
     */
    @SqlUpdate("INSERT INTO MESSAGES (MESSAGE_KEY,SENDER,RECEIVER,MESSAGE_TYPE ,MESSAGE_VALUE,MESSAGE_META_DATA) VALUES (:message_key,:sender,:receiver,:message_type,:message_value,:message_meta_data)")
    public abstract int addMessageRow(@BindBean final Message message);

    /**
     * Increments count of total messages between two users.
     * Should NOT be called directly cause of transactional nature
     * Note: Use  addMessage instead.
     * @param message_key
     * @return
     */
    @SqlUpdate("INSERT INTO USERS_MESSAGE_COUNT (MESSAGE_KEY,MESSAGE_COUNT) VALUES(:message_key,1) ON DUPLICATE KEY UPDATE MESSAGE_COUNT=MESSAGE_COUNT+1;")
    public abstract int addMessageCount(@Bind("message_key") String message_key);

    /**
     * Adds a message into DB and increments total message count between two users.
     * @param message
     */
    @Transaction
    public  void addMessage(Message message){
        addMessageCount(message.getMessage_key());
        addMessageRow(message);

    }

    /**
     * Returns count of Total number of messages between two users.
     * @param messageKey
     * @return
     */
    @SqlQuery("select MESSAGE_COUNT from USERS_MESSAGE_COUNT where MESSAGE_KEY=:message_key")
    public abstract int getTrueMessageCount(@Bind("message_key") String messageKey);

    /**
     * Gets latest 1000 messages between  two users
     * @param message_key
     * @return
     */
    @SqlQuery("select * from MESSAGES where MESSAGE_KEY=:message_key ORDER BY CREATE_DATE DESC LIMIT 1000")
    public abstract List<Message> getRecentMessages(@Bind("message_key") String message_key);

    /**
     * Gets messages for pagination. Needs pagenumber and paze size
     * @param message_key
     * @param limitParam
     * @param pagesize
     * @return
     */
    @SqlQuery("select * from challenge.MESSAGES where MESSAGE_KEY=:message_key order by CREATE_DATE desc limit :start,:pagesize;")
    public abstract List<Message> getMessagesByPagination(@Bind("message_key") String message_key, @Bind("start") int limitParam, @Bind("pagesize") int pagesize);

    @SqlQuery("select username from USERS order by username")
    public abstract List<String> getAllUser() ;
}
