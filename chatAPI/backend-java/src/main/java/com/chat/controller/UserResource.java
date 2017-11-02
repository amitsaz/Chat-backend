package com.chat.controller;

import com.chat.exceptions.UserAlreadyExistsException;
import com.chat.model.User;
import com.chat.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by asazawal on 10/21/17.
 *
 * User controller/Resource handler that saves a user in db.
 */

@Api("/users")
@Path("/users")
public class UserResource {

    private final ChatService chatService;

    public UserResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @ApiOperation(value = "Adds a new User into the system.")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public  Response addUser(@Valid User user) throws UserAlreadyExistsException, Exception {

      try {

          int userid = chatService.addUser(user);
          return Response.ok("{\"message\":\"User "+user.getUsername()+" created\"}").build();
      }
      catch (Exception e) {
          if(e.getCause() instanceof UserAlreadyExistsException){
           UserAlreadyExistsException ex= (UserAlreadyExistsException)e.getCause();
           throw ex;

          }
          else throw e ;

      }


    }

    @ApiOperation(value = "Shows all users in the system.")

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(){
        List<String> users=chatService.getAllUser();
        return Response.ok(users).build();

    }
}
