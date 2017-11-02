package com.chat.controller;

import com.chat.exceptions.UserNotFoundException;
import com.chat.model.Message;
import com.chat.model.MessageListWrapper;
import com.chat.model.MessageRequest;
import com.chat.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by asazawal on 10/21/17.
 * Controller/Resource handler for getting Messages between users.
 */

@Api("/message")
@Path("/message")
public class MessageResource {

    private static final Logger log = Logger.getLogger(MessageResource.class.getName());
    private final ChatService chatService;

    public MessageResource(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Captures message between two valid users
     * and updates the total no of messages.
     * @param message
     * @return
     * @throws UserNotFoundException
     * @throws Exception
     */
    @ApiOperation(value = "Captures message between two valid users and updates the total no of messages.")

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMessage(@Valid Message message) throws UserNotFoundException, Exception {

        int added=0;
        try {
             added = chatService.addMessage(message);
        }
        catch (Exception e) {
            if(e.getCause() instanceof UserNotFoundException){
                UserNotFoundException ex= (UserNotFoundException)e.getCause();
                throw ex;

            }
            else throw e ;

        }
        log.info("Added = "+added);
        return Response.ok("{\"message\":\"Message created\"}").build();

    }


    /**
     * Returns messages between two valid users sorted by date.
     * It uses a smart caching mechanism to retrieve messages and also
     * invalidates cache if data has changed in backend.
     * Also supports pagination using pagenum and pagesize params.
     * @param user1
     * @param user2
     * @param pagenum
     * @param pagesize
     * @return
     * @throws UserNotFoundException
     * @throws Exception
     */
    @ApiOperation("Returns messages between two valid users sorted by date. " +
                   "It uses a smart caching mechanism to retrieve messages and also invalidates" +
                   " cache if data has changed in backend. Also supports pagination using pagenum" +
                   " and pagesize params.")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(
            @NotNull
            @QueryParam("user1") String user1,
            @NotNull
            @QueryParam("user2") String user2,
            @QueryParam("pagenum") int pagenum,
            @QueryParam("pagesize") int pagesize

    ) throws UserNotFoundException, Exception {

        MessageRequest mr=new MessageRequest(user1,user2,pagenum,pagesize);
        MessageListWrapper messageWrapper = null ;

        try {
            messageWrapper =chatService.getMessagesCacheAware(mr);
        }
        catch (Exception e) {
            if(e.getCause() instanceof UserNotFoundException){
                UserNotFoundException ex= (UserNotFoundException)e.getCause();
                throw ex;

            }
            else throw e ;

        }
        return Response.ok(messageWrapper).build();

    }
}
