package io.d4tzz.dbproject.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.SQLException;

@ApplicationScoped
public class DataSourceConfig {
    @Produces
    public OracleDataSource oracleDataSource() throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        ds.setDriverType("oracle.jdbc.OracleDriver");
        ds.setURL("jdbc:oracle:thin:@localhost:1521:free");
        ds.setUser("c##dbproject");
        ds.setPassword("1");

        ds.getConnection();

        return ds;
    }

}
