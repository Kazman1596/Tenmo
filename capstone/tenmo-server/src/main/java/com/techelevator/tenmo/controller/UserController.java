package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserDao userDao;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> getUsers() {
        List<User> users = userDao.getUsers();
        if (users.size() == 0) {
            System.out.println("No users found.");
        }
        return users;
    }


    @RequestMapping(path = "/name/{name}", method = RequestMethod.GET)
    public User getUserByName(@PathVariable String name) {
        User user = userDao.getUserByUsername(name);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user.");
        } else {
            return user;
        }
    }

    @RequestMapping(path = "/id/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user.");
        } else {
            return user;
        }
    }


}
