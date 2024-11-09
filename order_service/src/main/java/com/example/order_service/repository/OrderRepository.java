package com.example.order_service.repository;

import com.example.order_service.model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Orders> getAllOrders() {
        String sql = "SELECT * FROM Orders";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> {
            Orders orders = new Orders();
            orders.setOrder_id(rs.getInt("order_id"));
            orders.setUser_id(rs.getInt("user_id"));
            orders.setOrder_date(rs.getTimestamp("order_date"));
            orders.setCreate_at(rs.getTimestamp("create_at"));
            orders.setUpdate_at(rs.getTimestamp("update_at"));
            orders.setStatus(rs.getString("status"));
            orders.setTotal_amount(rs.getBigDecimal("total_amount"));
            return orders;
        }));
    }

    public Orders getOrderById(int order_id) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        return jdbcTemplate.query();
    }
}
