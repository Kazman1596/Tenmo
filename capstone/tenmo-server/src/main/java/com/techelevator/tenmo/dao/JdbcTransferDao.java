package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        //TODO: SELECT * should be individually typed out for all
        String sql = "SELECT * FROM account WHERE user_id =?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                account = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public Transfer getAccountFromIdFromTransferId(int id) {
        return null;
    }

    @Override
    public Transfer getAccountToIdFromTransferId(int id) {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {

    }
}
