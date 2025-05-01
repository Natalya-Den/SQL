package ru.netology.login.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    @SneakyThrows
    public static String getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var connect = getConnect()) {
            return QUERY_RUNNER.query(connect, codeSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var connect = getConnect()) {
            QUERY_RUNNER.execute(connect, "DELETE FROM auth_codes");
            QUERY_RUNNER.execute(connect, "DELETE FROM card_transactions");
            QUERY_RUNNER.execute(connect, "DELETE FROM cards");
            QUERY_RUNNER.execute(connect, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static void cleanAuthCode() {
        try (var connect = getConnect()) {
            QUERY_RUNNER.execute(connect, "DELETE FROM auth_codes");
        }
    }
}
