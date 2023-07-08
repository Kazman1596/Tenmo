package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {
    public static String API_BASE_URL = "http://localhost:8080/transfer/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Transfer getTransferByTransferId(int id) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.getForObject(API_BASE_URL + id, Transfer.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfer;
    }
    public Transfer[] getTransfersByAccountId(int id) {
        Transfer[] transferList = null;

        try{
            transferList = restTemplate.getForObject(API_BASE_URL + "user/" + id, Transfer[].class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferList;
    }

    public Transfer[] getPendingTransfers(int id, int transferStatusId) {
        Transfer[] pendingTransfers = null;

        try {
            String url = API_BASE_URL + "user/" + id + "?transferStatusId=" + transferStatusId;
            pendingTransfers = restTemplate.getForObject(url, Transfer[].class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingTransfers;
    }

    public Transfer createTransfer(Transfer newTransfer) {
        String url = API_BASE_URL;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);
        Transfer result = null;

        try {
            result = restTemplate.postForObject(url, entity, Transfer.class);
        }
        catch(RestClientResponseException ex){
            BasicLogger.log(ex.getRawStatusCode() + ":" + ex.getStatusText());
        }
        catch(ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }

        return result;
    }

    public boolean updateTransfer(Transfer updatedTransfer) {
        String url = API_BASE_URL + updatedTransfer.getTransferId();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transfer> entity = new HttpEntity<>(updatedTransfer, headers);
        boolean success = false;
        try {
            restTemplate.put(url, entity);
            success = true;
        }
        catch(RestClientResponseException ex){
            BasicLogger.log(ex.getRawStatusCode() + ":" + ex.getStatusText());
        }
        catch(ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }

        return success;
    }



}

