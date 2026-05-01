package by.psu.dao;

import by.psu.model.Excursion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
public class JdbcHelper {

    private final Connection connection;

    // Конструктор с Connection (для транзакций)
    public JdbcHelper(Connection connection) {
        this.connection = connection;
    }

    // Пустой конструктор (для статических методов)
    public JdbcHelper() {
        this.connection = null;
    }

    // Поиск по ID
    public Excursion findExcursionById(int id) throws SQLException {
        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, excursion_type, lunch_included " +
                "FROM excursion WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToExcursion(rs);
            }
        }
        return null;
    }

    // Сохранение (INSERT или UPDATE)
    public void saveExcursion(Excursion excursion) throws SQLException {
        if (excursion.getId() == null || excursion.getId() == 0) {
            // INSERT
            String sql = "INSERT INTO excursion (name, price, \"from\", \"to\", guide_name, excursion_type, lunch_included) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, excursion.getName());
                pstmt.setBigDecimal(2, excursion.getPrice());
                pstmt.setDate(3, Date.valueOf(excursion.getFrom()));
                pstmt.setDate(4, Date.valueOf(excursion.getTo()));
                pstmt.setString(5, excursion.getGuideName());
                pstmt.setString(6, excursion.getExcursionType());
                pstmt.setBoolean(7, excursion.isLunchIncluded());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    excursion.setId(rs.getInt("id"));
                }
            }
        } else {
            // UPDATE
            String sql = "UPDATE excursion SET name = ?, price = ?, \"from\" = ?, \"to\" = ?, " +
                    "guide_name = ?, excursion_type = ?, lunch_included = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, excursion.getName());
                pstmt.setBigDecimal(2, excursion.getPrice());
                pstmt.setDate(3, Date.valueOf(excursion.getFrom()));
                pstmt.setDate(4, Date.valueOf(excursion.getTo()));
                pstmt.setString(5, excursion.getGuideName());
                pstmt.setString(6, excursion.getExcursionType());
                pstmt.setBoolean(7, excursion.isLunchIncluded());
                pstmt.setInt(8, excursion.getId());
                pstmt.executeUpdate();
            }
        }
    }

    // Найти все экскурсии
    public List<Excursion> findAllExcursions() throws SQLException {
        List<Excursion> excursions = new ArrayList<>();
        String sql = "SELECT id, name, price, \"from\", \"to\", guide_name, excursion_type, lunch_included " +
                "FROM excursion ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                excursions.add(mapResultSetToExcursion(rs));
            }
        }
        return excursions;
    }

    // Вспомогательный метод для преобразования ResultSet в Excursion
    private Excursion mapResultSetToExcursion(ResultSet rs) throws SQLException {
        Excursion excursion = new Excursion();
        excursion.setId(rs.getInt("id"));
        excursion.setName(rs.getString("name"));
        excursion.setPrice(rs.getBigDecimal("price"));
        LocalDate fromDate = rs.getDate("from") != null ? rs.getDate("from").toLocalDate() : null;
        LocalDate toDate = rs.getDate("to") != null ? rs.getDate("to").toLocalDate() : null;
        excursion.setFrom(fromDate);
        excursion.setTo(toDate);
        excursion.setGuideName(rs.getString("guide_name"));
        excursion.setExcursionType(rs.getString("excursion_type"));
        excursion.setLunchIncluded(rs.getBoolean("lunch_included"));
        return excursion;
    }

    // ========== СТАТИЧЕСКИЕ МЕТОДЫ ДЛЯ ВАШЕГО КОДА ==========

    private static JdbcHelper getHelper() {
        try {
            return new JdbcHelper(ConnectionManager.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Excursion> getAllExcursions() {
        try {
            return getHelper().findAllExcursions();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Excursion getExcursionById(int id) {
        try {
            return getHelper().findExcursionById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}