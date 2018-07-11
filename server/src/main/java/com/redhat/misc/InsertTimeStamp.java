package com.redhat.client;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Created by lyu on 06/07/17.
 */
public class InsertTimeStamp {

   // setTimestamp(new java.sql.Timestamp(java.util.Date.getTime))
   // resultSet.getTimestamp(17)


    private void dbLookup (String sqlString) {
        Context ctx = null;
        Connection connection= null;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("jdbc/ora12");
            connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlString);
           ps.execute();
            ResultSet resultSet= ps.getResultSet();

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
