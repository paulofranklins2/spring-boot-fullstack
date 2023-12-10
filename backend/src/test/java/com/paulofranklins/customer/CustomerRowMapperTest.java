package com.paulofranklins.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        var customerRowMapper = new CustomerRowMapper();

        var resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Paulo");
        when(resultSet.getString("email")).thenReturn("paulofranklinc@gmail.com");
        when(resultSet.getInt("age")).thenReturn(20);
        when(resultSet.getString("gender")).thenReturn("MALE");

        //When
        var actual = customerRowMapper.mapRow(resultSet, 1);

        //Then
        var expected = new Customer(
                1, "Paulo", "paulofranklinc@gmail.com", 20, Gender.MALE);

        assertThat(actual).isEqualTo(expected);
    }
}