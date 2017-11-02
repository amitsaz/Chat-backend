package com.chat.model;

import java.util.List;

/**
 * Created by asazawal on 10/21/17.
 *
 * Wrapper for Message data.
 * This will be stored in cache and also used to
 * create response for the client.
 */

public class MessageListWrapper {

    private List<Message> messages;
    private int totalMessages ;
    private int pagenum ;
    private int pagesize;

    public MessageListWrapper(List<Message> messages, int totalMessages, int pagenum, int pagesize) {
        this.messages = messages;
        this.totalMessages = totalMessages;
        this.pagenum = pagenum;
        this.pagesize = pagesize;

    }
    public MessageListWrapper (){};


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }


}
