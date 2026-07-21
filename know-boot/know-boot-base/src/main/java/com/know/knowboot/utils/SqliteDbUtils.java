package com.know.knowboot.utils;


import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class SqliteDbUtils {


//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//
//        Class.forName("org.sqlite.JDBC");
//        Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\123456.db");
//
//        func1(conn);
//
//        conn.close();
//
//    }

    public static void func1(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30); // set timeout to 30 sec.
        // 执行查询语句
        ResultSet rs = statement.executeQuery("select * from keycerts");
        int num = 0;

        while (rs.next()) {
            log.info("---------------------------------------------开始-------------------------------------------------");
            String col1 = rs.getString("shell_num");
            String col2 = rs.getString("order_id");
            String col3 = rs.getString("cert_type");
            String col4 = rs.getString("cert_content");

            log.info("shell_num -> {}",col1);
            log.info("order_id -> {}",col2);
            log.info("cert_type -> {}",col3);
            log.info("cert_content -> {}",col4);

            log.info("---------------------------------------------结束-------------------------------------------------");

            num++;
        }
        System.out.println(num);
    }

}
