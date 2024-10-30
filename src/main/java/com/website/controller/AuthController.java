package com.website.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.website.config.JwtProvider;
import com.website.exception.UserException;
import com.website.model.Cart;
import com.website.model.User;
import com.website.repository.UserRepository;
import com.website.request.LoginRequest;
import com.website.response.AuthResponse;
import com.website.service.CartService;
import com.website.service.CustomeUserServiceImplementation;
@RestController
@RequestMapping("/auth")
public class AuthController {
	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	private PasswordEncoder passwordEncoder;
	private CustomeUserServiceImplementation customUserService;
	private CartService cartService;
	public AuthController(UserRepository userRepository
							,JwtProvider jwtProvider
							,PasswordEncoder passwordEncoder
							,CustomeUserServiceImplementation customUserService
							,CartService cartService) {
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
		this.passwordEncoder = passwordEncoder;
		this.customUserService = customUserService;
		this.cartService = cartService;
	}
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		String email  = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		User isEmailExist=userRepository.findByEmail(email);
		if(isEmailExist!=null)
		{
			throw new UserException("Email is already used with another account");
		}
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setFirstName(firstName);
		createdUser.setLastName(lastName);
		
		User savedUser = userRepository.save(createdUser);
		Cart cart = cartService.createCart(savedUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SIGNUP SUCCESS..");
		return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
	}
	
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		if(userDetails==null)
		{
			throw new BadCredentialsException("Invalid Username:(");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
//			System.out.println("sign in userDetails - password not match " + userDetails);
			throw new BadCredentialsException("Invalid Password :(");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	
	
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest)
	{
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		Authentication authentication=authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SIGNIN SUCCESS..");
		return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
	}

}