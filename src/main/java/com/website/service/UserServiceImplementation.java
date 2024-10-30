package com.website.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.website.config.JwtProvider;
import com.website.exception.UserException;
import com.website.model.User;
import com.website.repository.UserRepository;


@Service
public class UserServiceImplementation implements UserService{

	
	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	public UserServiceImplementation(UserRepository userRepository
			,JwtProvider jwtProvider) {
		this.userRepository=userRepository;
		this.jwtProvider=jwtProvider;
	}
	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) 
		{
			return user.get();
		}
		throw new UserException("user not found with id : "+userId);
	}

	@Override
	public User findUserProfileByJwt(String Jwt) throws UserException {
		String email = jwtProvider.getEmailFromToken(Jwt);
		User user = userRepository.findByEmail(email);
		if(user==null)
		{
			throw new UserException("user not found with email "+email);
		}
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}
