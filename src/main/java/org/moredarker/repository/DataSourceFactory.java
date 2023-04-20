package org.moredarker.repository;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
    public static DataSource getMySQLDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();

        mysqlDataSource.setURL("jdbc:mysql://localhost:3306/my_db?serverTimezone=Europe/Moscow&useSSL=false");
        mysqlDataSource.setUser("bestuser");
        mysqlDataSource.setPassword("bestuser");

        return mysqlDataSource;
    }
}
