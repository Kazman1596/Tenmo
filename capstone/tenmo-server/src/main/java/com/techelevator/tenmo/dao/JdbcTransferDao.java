package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to FROM account WHERE transfer_id =?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public Transfer getAccountFromIdFromTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT account_from FROM account WHERE transfer_id =?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public Transfer getAccountToIdFromTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT account_to FROM account WHERE transfer_id =?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public String getTransferStatusByTransferId(int transferId) {
       String transferDesc = "";
        String sql = "SELECT transfer_status_desc\n" +
                "FROM transfer t\n" +
                "JOIN transfer_status ts\n" +
                "\tON t.transfer_status_id = ts.transfer_status_id WHERE transfer_id =?;";
        try {
           transferDesc = jdbcTemplate.queryForObject(sql, String.class,transferId);
            if (transferDesc.equals("")) {
                throw new DaoException("No status message found.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferDesc;
    }

    @Override
    public String getTransferTypeByTransferId(int transferId) {
        String transferTypeDesc = "";
        String sql = "SELECT transfer_type_desc\n" +
                "FROM transfer t\n" +
                "JOIN transfer_type tt\n" +
                "\tON t.transfer_type_id = tt.transfer_type_id WHERE transfer_id =?;";
        try {
            transferTypeDesc = jdbcTemplate.queryForObject(sql, String.class,transferId);
            if (transferTypeDesc.equals("")) {
                throw new DaoException("No status message found.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferTypeDesc;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFromId(rs.getInt("account_from"));
        transfer.setAccountToId(rs.getInt("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        return transfer;

    }
}
