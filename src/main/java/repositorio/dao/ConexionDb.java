package repositorio.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 54346
 */

public class ConexionDb {

    private String url = "jdbc:mysql://localhost:3306/db_bicicleteria"; 
    private String userDb = "root";
    private String password = ""; 
    
    public ConexionDb() {
        super();
    }
    
    public Connection dbConection() {
        Connection conn = null;
        System.out.println("url: " + url);
        
        try {
            conn = DriverManager.getConnection(url, userDb, password);
            System.out.println("Connected to the mySQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }

    public ResultSet executeSqlQuery(String sql) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error al ejecutar la consulta SQL");
        }

        return rs;
    }
    
    public int executeSqlUpdateWithParameters(String sql, HashMap<Integer, Object> param) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsAffected = 0;  
        try {
            conn = dbConection();  

            pstmt = conn.prepareStatement(sql);

            for (Integer index : param.keySet()) {
                Object obj = param.get(index);
                if (obj instanceof Integer) {
                    pstmt.setInt(index + 1, (Integer) obj);
                } else if (obj instanceof String) {
                    pstmt.setString(index + 1, (String) obj);
                } else if (obj instanceof Float) {
                    pstmt.setFloat(index + 1, (Float) obj);
                } else if (obj instanceof Double) {
                    pstmt.setDouble(index + 1, (Double) obj);
                } else if (obj instanceof Boolean) {
                    pstmt.setBoolean(index + 1, (Boolean) obj);
                } else if (obj instanceof Long) {
                    pstmt.setLong(index + 1, (Long) obj);
                } else if (obj instanceof Date) {
                    pstmt.setDate(index + 1, new java.sql.Date(((Date) obj).getTime()));
                }
            }

            rowsAffected = pstmt.executeUpdate();  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error al ejecutar la consulta de actualización");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return rowsAffected;  
    }
    
    public ResultSet executeSqlQueryWithParameters(String sql, HashMap<Integer, Object> param) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbConection();  

            pstmt = conn.prepareStatement(sql);

            for (Integer index : param.keySet()) {
                Object obj = param.get(index);
                if (obj instanceof Integer) {
                    pstmt.setInt(index + 1, (Integer) obj);
                } else if (obj instanceof String) {
                    pstmt.setString(index + 1, (String) obj);
                } else if (obj instanceof Float) {
                    pstmt.setFloat(index + 1, (Float) obj);
                } else if (obj instanceof Double) {
                    pstmt.setDouble(index + 1, (Double) obj);
                } else if (obj instanceof Boolean) {
                    pstmt.setBoolean(index + 1, (Boolean) obj);
                } else if (obj instanceof Long) {
                    pstmt.setLong(index + 1, (Long) obj);
                } else if (obj instanceof Date) {
                    pstmt.setDate(index + 1, new java.sql.Date(((Date) obj).getTime()));
                }
            }

            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error al ejecutar la consulta SQL");
        }

        return rs;
    }
    
    public void executeSqlWithParameters(String sql, HashMap<Integer, Object> param) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbConection(); 
            pstmt = conn.prepareStatement(sql);

            for (Integer index : param.keySet()) {
                Object obj = param.get(index);
                if (obj instanceof Integer) {
                    pstmt.setInt(index + 1, (Integer) obj);
                } else if (obj instanceof String) {
                    pstmt.setString(index + 1, (String) obj);
                } else if (obj instanceof Float) {
                    pstmt.setFloat(index + 1, (Float) obj);
                } else if (obj instanceof Double) {
                    pstmt.setDouble(index + 1, (Double) obj);
                } else if (obj instanceof Boolean) {
                    pstmt.setBoolean(index + 1, (Boolean) obj);
                } else if (obj instanceof Long) {
                    pstmt.setLong(index + 1, (Long) obj);
                } else if (obj instanceof Date) {
                    pstmt.setDate(index + 1, new java.sql.Date(((Date) obj).getTime()));
                }
            }

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                System.out.println("ID generado: " + generatedId);
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            System.out.println(e.getMessage());
            throw new SQLException("Error conexión en executeSqlQueryWithParameters sin RS");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}