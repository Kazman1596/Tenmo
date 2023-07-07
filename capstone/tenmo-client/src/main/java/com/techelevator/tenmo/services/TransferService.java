package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {
    public static String API_BASE_URL = "http://localhost:8080/transfer/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Transfer[] getTransfersByUserId(int id) {
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



}

