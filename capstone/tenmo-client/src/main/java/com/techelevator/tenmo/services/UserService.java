package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    String API_BASE_URL = "http://localhost:8080/user/";
    private final RestTemplate restTemplate = new RestTemplate();

    public User[] getUsers() {
        User[] users = null;
        try{
            users = restTemplate.getForObject(API_BASE_URL, User[].class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }
    public User getUserById(int id) {
        User user = null;
        try{
            user = restTemplate.getForObject(API_BASE_URL + "id/" + id, User.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public User getUserByName(String name) {
        User user = null;
        try{
            user = restTemplate.getForObject(API_BASE_URL + name, User.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }
}
