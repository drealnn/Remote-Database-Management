/**
 * Created by Sledd on 4/3/2016.
 */

import com.sun.javafx.geom.AreaOp;
import com.sun.rowset.CachedRowSetImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;


public class MySqlServlet extends HttpServlet {

    ResultSet cursor;
    Connection connection;

    @Override
    public void init() throws ServletException {
        // connect to database here




            //cursor = new CachedRowSetImpl();

            /*
            cursor.setUrl("jdbc:mysql://localhost:3306/project4");
            cursor.setUsername("root");
            cursor.setPassword("root");
            */




    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // grab sql query from request

        String sqlQuery = request.getParameter("sqlquery");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project4", "root", "root");

            // if query is select, execute cacherow

            if (sqlQuery.toLowerCase().contains("select"))
            {
                Statement statement = connection.createStatement();
                cursor = statement.executeQuery(sqlQuery);
                //cursor.setCommand(sqlQuery);
                //cursor.execute();

                ResultSetMetaData metaData = cursor.getMetaData();
                int numCols = metaData.getColumnCount();

                cursor.last();


                String[][] mytable = new String[cursor.getRow()+1][numCols];


                for (int i = 1; i < numCols+1; i++)
                {
                    mytable[0][i-1] = metaData.getColumnName(i);

                } // end for

                cursor.beforeFirst();
                int j = 1;
                while(cursor.next()) {
                    //for (Collection<String> n  : cursor.toCollection().iterator())
                    for (int i = 1; i < numCols+1; i++) {
                        mytable[j][i-1] = cursor.getString(i);
                    }

                    //System.out.println();
                    j++;
                } // end while

                request.setAttribute("table" , mytable);


            } // end if
            else //if (sqlQuery.toLowerCase().contains("insert"))
            {
                String quantityQuery = "select distinct snum from shipments where quantity >= 100;";
                Statement statement = connection.createStatement();

                // select and store distinct snums where results > 100
                ResultSet resultSet = statement.executeQuery(quantityQuery);
                HashSet<String> storedSnums = new HashSet<>();

                resultSet.beforeFirst();
                while (resultSet.next())
                {
                    storedSnums.add(resultSet.getString(1));
                }


                // make update/insertion query
                statement.executeUpdate(sqlQuery);
                int rowsChanged = statement.getUpdateCount();

                // select and store distinct snums where results > 100
                ResultSet resultSet2 = statement.executeQuery(quantityQuery);

                // compare two sets
                ArrayList<String> newSnums = new ArrayList<>();
                resultSet2.beforeFirst();
                while (resultSet2.next())
                {
                    if (!storedSnums.contains(resultSet2.getString(1)))
                        newSnums.add(resultSet2.getString(1));

                }

               // make a new update query to up the status by 5 for each snum who's quantity >= 100
                for (String n : newSnums) {
                    String updateQuery = String.format("update suppliers set status = status + 5 where snum = \'%s\'",  n);
                    statement.executeUpdate(updateQuery);
                }





                // make insertion and make selection query listing all the ids with results > 100

                // send off the rows changed, and the number of supplier updates
                request.setAttribute("rowsChanged", String.valueOf(rowsChanged));
                request.setAttribute("statusMarks", String.valueOf(newSnums.size()));



            }

            request.getRequestDispatcher("index.jsp").forward(request, response);



        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
