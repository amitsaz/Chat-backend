package com.chat.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

/**
 * Created by asazawal on 10/21/17.
 *
 * POJO representing Message object
 * Contains validation for certain fields
 */
public class Message {

 public enum  MESSAGE_TYPE{

        TEXT("TEXT"),
        IMAGE("IMG"),
        VIDEO("VIDEO") ;

        private String type;

        MESSAGE_TYPE(String type) {
            this.type=type;
        }

        public String getType() {
            return type;
        }


    }




    public Message(String message_key, String sender, String receiver, Timestamp create_date, String message_type, String message_value, String message_meta_data){
       this.message_key=message_key;
       this.sender=sender;
       this.receiver=receiver;
       this.create_date=create_date;
       this.message_type=message_type;
       this.message_value=message_value;
       this.message_meta_data=message_meta_data;
    }

    public Message(){}

    private String message_key ;
    @NotNull
    private String sender ;
    @NotNull
    private String receiver ;
    private Timestamp create_date ;
    @NotNull
    @Pattern(regexp = "text|img|video")
    private String message_type ;
    private String message_value;
    private String message_meta_data;

    public String getMessage_key() {
        return message_key;
    }

    public void setMessage_key(String message_key) {
        this.message_key = message_key;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Timestamp getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_value() {
        return message_value;
    }

    public void setMessage_value(String message_value) {
        this.message_value = message_value;
    }

    public String getMessage_meta_data() {
        return message_meta_data;
    }

    public void setMessage_meta_data(String message_meta_data) {
        this.message_meta_data = message_meta_data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_key='" + message_key + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", create_date=" + create_date +
                ", message_type='" + message_type + '\'' +
                ", message_value='" + message_value + '\'' +
                ", message_meta_data='" + message_meta_data + '\'' +
                '}';
    }
}
