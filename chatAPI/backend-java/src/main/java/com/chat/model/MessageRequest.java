package com.chat.model;

import com.chat.service.ChatUtils;

import javax.validation.constraints.NotNull;

/**
 * Created by asazawal on 10/21/17.
 *
 * POJO representing request for getting messages between users.
 */
public class MessageRequest {

    public MessageRequest(){}

    public MessageRequest(String user1, String user2, int pagenum, int messagesperpage) {
        this.user1 = user1;
        this.user2 = user2;
        this.pagenum = pagenum;
        this.pagesize = messagesperpage;
        if(this.user1 != null && this.user2 != null)
            this.message_key = ChatUtils.getMessageKey(this.user1,this.user2);
    }

    @NotNull
    private String user1 ;
    @NotNull
    private String user2;
    private int pagenum ;
    private int pagesize;
    private String message_key ;

    public String getMessage_key() {
        return message_key;
    }

    public void setMessage_key(String message_key) {
        this.message_key = message_key;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
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
