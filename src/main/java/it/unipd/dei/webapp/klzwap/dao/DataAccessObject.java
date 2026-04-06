package it.unipd.dei.webapp.klzwap.dao;

import java.sql.SQLException;

public interface DataAccessObject <T> {

    DataAccessObject<T> access() throws SQLException;

    T getOutputParam();
}
