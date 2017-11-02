package com.chat.service;

import com.chat.dao.ChatDao;
import com.chat.exceptions.ErrorCodes;
import com.chat.exceptions.UserAlreadyExistsException;
import com.chat.exceptions.UserNotFoundException;
import com.chat.model.Message;
import com.chat.model.MessageListWrapper;
import com.chat.model.MessageRequest;
import com.chat.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by asazawal on 10/21/17.
 *
 * Main service class that abstracts Database and cache operations.
 * Has methods for :
 * 1. adding users
 * 2. Getting messages.
 */
public abstract class ChatService {

    private static final Logger log= Logger.getLogger(ChatService.class.getName());

    @CreateSqlObject
    public abstract ChatDao chatDao();


    /*
      Simple in memory cache implementation
      Obviously this will be replaced by some high performing distributed cache implementation
      in production env.
     */
    LoadingCache<String, MessageListWrapper> messagecache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(
                    new CacheLoader<String, MessageListWrapper>() {
                        public MessageListWrapper load(String key) throws Exception {
                            log.info(key+ " Not found in cache. Getting from DB..");
                            String[] params=key.split("_");
                            return getMessagesFromDB(params[0],Integer.parseInt(params[1]),Integer.parseInt(params[2]),0);
                        }
                    });


    /**
     * Method that adds user in system.
     * @param user
     * @return
     * @throws UserAlreadyExistsException
     */
    public int addUser(User user) throws UserAlreadyExistsException {

        try {

            return chatDao().addUser(user);
        } catch (Exception e) {

            if (e.getCause() instanceof SQLIntegrityConstraintViolationException)
               throw new UserAlreadyExistsException(ErrorCodes.USER_ALREADY_EXISTS);

            else
                throw e;
        }


    }


    /**
     * Method that adds a new message between two valid users.
     * @param message
     * @return
     * @throws UserNotFoundException
     */
    public int addMessage(Message message) throws UserNotFoundException {

        message.setMessage_key(ChatUtils.getMessageKey(message.getSender(),message.getReceiver()));

        try {
            message.setMessage_meta_data(ChatUtils.getMetaData(message.getMessage_type()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            chatDao().addMessage(message);
        }catch (Exception e){

            if (e.getCause() instanceof SQLIntegrityConstraintViolationException)
                throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND);

            else
                throw e;
        }

        return 1;

    }

    /**
     * Retrieves messages between two valid users.
     * First checks in cache and if data found, compares the
     * total message count  from cache entry against message count from database.
     * Count from database is from a small table that
     * just stores the total message count for the given users
     * and is indexed, hence retrieval is super quick.
     * If count is same, cache is valid.
     * If count is different, that means more messages have been sent between these two users.
     * In that case, makes DB calls , returns data and updates cache.
     *
     * Also supports pagination.
     * @param mr
     * @return
     */
    public MessageListWrapper getMessagesCacheAware(MessageRequest mr) {

        String cacheKey = new StringBuilder().append(mr.getMessage_key())
                                             .append("_").append(mr.getPagenum())
                                             .append("_").append(mr.getPagesize())
                                             .toString();

        MessageListWrapper wrapper= null;

        //true message count
        int trueMessageCount= chatDao().getTrueMessageCount(mr.getMessage_key());
        log.info("Actual message count from DB: "+trueMessageCount);


        try {
            wrapper = messagecache.get(cacheKey);
        } catch (ExecutionException e) {
            e.printStackTrace();
            wrapper =getMessagesFromDB(mr.getMessage_key(),mr.getPagenum(),mr.getPagesize(),trueMessageCount);
        }

        if (wrapper.getTotalMessages()!=trueMessageCount) {

            log.info("Cache is stale since total message count is differnt.");
            wrapper = getMessagesFromDB(mr.getMessage_key(),mr.getPagenum(),mr.getPagesize(),trueMessageCount);
            messagecache.put(cacheKey,wrapper);

        }

        return wrapper;


    }

    public MessageListWrapper getMessagesFromDB(String message_key,int pagenum , int pagesize, int totalMessageCount){

        List<Message> messages= Collections.emptyList();
        MessageListWrapper wrapper = null ;
        int totalMsgcount;

        if (totalMessageCount == 0)
            totalMsgcount = chatDao().getTrueMessageCount(message_key);
        else
            totalMsgcount = totalMessageCount;

        //if no pagination requested , send 1000 records by default along with size
        //so client can request pagination accordingly. We will also store these 1000 records in
        //cache .

        if(pagenum == 0 && pagesize == 0){

            messages =chatDao().getRecentMessages(message_key);
            wrapper = new MessageListWrapper(messages,totalMsgcount,pagenum,pagesize);

            return wrapper;

        }

        //if pagination requested, get data accordingly
        // to create sql query , we need arguments for the SQL limit functionality.
        //For this we need total count which we are getting from a separate small table
        //indexed by primary key ,so its blazing fast.
        //Once we have the count,using simple maths, we can get the limit arguments using this formula,
        //  (pagenum-1)*pagesize , pagesize

        if(pagenum >= 1 && pagesize >= 1){

            int limitParam=(pagenum-1) * pagesize ;
            messages =chatDao().getMessagesByPagination(message_key,limitParam,pagesize);
            wrapper = new MessageListWrapper(messages,totalMsgcount,pagenum,pagesize);

            return wrapper;

        }

        if(wrapper==null)
        return new MessageListWrapper(Collections.EMPTY_LIST,0,0,0);
        else
            return wrapper;



    }


    public List<String> getAllUser() {

        try {


            List<String> users = chatDao().getAllUser() ;

                return users;

        }catch (Exception e){

            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
