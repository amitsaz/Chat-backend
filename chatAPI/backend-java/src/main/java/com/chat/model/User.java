package com.chat.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by asazawal on 10/21/17.
 *
 * POJO representing a User in system.
 * Has validations.
 */
public class User {

    @NotNull
    @Size(min = 3, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;

    @NotNull
    @Size(min = 3, max = 15)
    private String passwd;


    public User() {

    }

    public User(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


}
