package com.website.service;

import java.util.List;

import com.website.exception.UserException;
import com.website.model.User;


public interface UserService {
	public User findUserById(Long userId) throws UserException;
	public User findUserProfileByJwt(String Jwt) throws UserException;	
	public List<User> findAllUsers();

}
