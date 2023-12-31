package com.paulofranklins.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }


    @Override
    public List<Customer> selectAllCustomers() {
        var sql = "SELECT * FROM customer LIMIT 50";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = "SELECT * FROM customer WHERE id = ?";
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = "INSERT INTO customer(name, email, password, age, gender) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getPassword(), customer.getAge(), customer.getGender().name());
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        var sql = "SELECT count(id) FROM customer WHERE email = ?";
        var i = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return i != null && i > 0;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        var sql = "SELECT count(id) FROM customer WHERE id = ?";
        var i = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return i != null && i > 0;
    }

    @Override
    public void deleteCustomer(Integer id) {
        var sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer update) {

        if (update.getName() != null) {
            var sql = "UPDATE customer SET  name = ? where id = ? ";
            jdbcTemplate.update(sql, update.getName(), update.getId());
        }

        if (update.getEmail() != null) {
            var sql = "UPDATE customer SET  email = ? where id = ? ";
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }

        if (update.getAge() != null) {
            var sql = "UPDATE customer SET  age = ? where id = ? ";
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        var sql = "SELECT * FROM customer WHERE email = ?";
        return jdbcTemplate.query(sql, customerRowMapper, email)
                .stream().findFirst();
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId, Integer customerId) {
        var sql = "UPDATE customer SET profile_image_id = ? where id = ?";
        jdbcTemplate.update(sql, profileImageId, customerId);
    }
}
