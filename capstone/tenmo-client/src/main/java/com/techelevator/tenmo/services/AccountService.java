package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class AccountService {
    public static String API_BASE_URL = "http://localhost:8080/account/";

    private final RestTemplate restTemplate = new RestTemplate();

    public Account getAccount(int id) {
        Account result = null;
        try{

            result = restTemplate.getForObject(API_BASE_URL + id, Account.class);

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    public Account getAccountFromUserId(int id) {
        Account result = null;
        try{
            result = restTemplate.getForObject(API_BASE_URL + "/user/" + id, Account.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }
}
