package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer createdTransfer = null;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?) RETURNING transfer_id;";
        try {
            int createdTransferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(), transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());

            createdTransfer = getTransferByTransferId(createdTransferId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);

        }
            return createdTransfer;
    }

    @Override
    public Transfer updateTransfer(Transfer transfer) {
        Transfer updatedTransfer = null;
                String sql = "UPDATE transfer SET transfer_type_id = ?, transfer_status_id = ?, account_from = ?, account_to = ?, amount = ? WHERE transfer_id =?;";
    try {
        int numberOfRows = jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount(), transfer.getTransferId());
    if (numberOfRows == 0) {
        throw new DaoException(("No rows affected, sorry."));
    } else {
        updatedTransfer = getTransferByTransferId(transfer.getTransferId());
    }
    } catch (CannotGetJdbcConnectionException e) {
        throw new DaoException("Unable to connect to server or database.", e);
    } catch (DataIntegrityViolationException e) {
        throw new DaoException("Data integrity violation", e);
    }
    return updatedTransfer;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id =?;";
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
    public List<Transfer> getTransfersByAccountId(int userId, int transferStatusId) {
        List<Transfer> transfersByUser= new ArrayList<>();
        String sql="SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE (account_to = ?";
        SqlRowSet results;

        if(transferStatusId > 0 && transferStatusId <= 3) {
            sql += ") AND transfer_status_id =?;";
            results = jdbcTemplate.queryForRowSet(sql, userId, transferStatusId);
        } else {
            sql += " OR account_from = ?);";
            results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        }

        try{
            while (results.next()) {
                transfersByUser.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfersByUser;

    }


/*
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
 */


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
