package com.dmden22.spring.mvc_hibernate.service;

import com.dmden22.spring.mvc_hibernate.model.User;

import java.util.List;

public interface UserService {

    public List<User> getUsersList();

    public void addUser(User user);

    public void deleteUser(Long id);

    public User getUserByID(long id);

    public void updateUser(long id, String newName, String newLastName, String newEmail);
}
