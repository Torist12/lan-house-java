package com.lanhouse.repository.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database operations.
 */
public final class DatabaseUtils {

    private DatabaseUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the last auto-incremented ID for the current connection.
     *
     * @param conn Active database connection.
     * @return The last inserted row ID, or -1 if retrieval fails.
     * @throws SQLException If database access fails.
     */
    public static int getLastInsertId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
}