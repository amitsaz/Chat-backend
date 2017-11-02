package com.chat.dao;

import com.chat.model.Message;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by asazawal on 10/21/17.
 * mapper for ChatDao.
 */
public class MessageMapper implements ResultSetMapper<Message> {
    @Override
    public Message map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Message(r.getString("message_key"),r.getString("sender"),r.getString("receiver"),
                r.getTimestamp("create_date"),r.getString("message_type"),r.getString("message_value"),
                r.getString("message_meta_data"));
    }
}
