package com.website.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.website.exception.OrderException;
import com.website.model.Order;
import com.website.repository.OrderRepository;
import com.website.response.ApiResponse;
import com.website.service.OrderService;
import com.website.service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	@Value("${razorpay.api.key}")
	String apiKey;
	
	@Value("${razorpay.api.secret}")
	String apiSecret;
	
	private OrderService orderService;
	private UserService userService;
	private OrderRepository orderRepository;
	
	public PaymentController(OrderService orderService,
			UserService userService,OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		this.userService = userService;
		this.orderService = orderService;
	}

	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, 
			@RequestHeader("Authorization")String jwt) throws OrderException, RazorpayException{
		Order order = orderService.findOrderById(orderId);
		try {  
			RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", order.getTotalPrice()*100); //rupees me convert
			paymentLinkRequest.put("currency","INR");
			
			JSONObject customer = new JSONObject();
			customer.put("name",order.getUser().getFirstName());
			customer.put("email",order.getUser().getEmail());
			paymentLinkRequest.put("customer",customer);
			
			JSONObject notify = new JSONObject();
			notify.put("sms", true);
			notify.put("email",true);
			paymentLinkRequest.put("notify",notify);
			
			paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId);
			paymentLinkRequest.put("callback_method","get");
			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			PaymentLinkResponse res = new PaymentLinkResponse();
			res.setPayment_link_id(paymentLinkId);
			res.setPayment_link_url(paymentLinkUrl);
			
			return new ResponseEntity<PaymentLinkResponse>(res,HttpStatus.CREATED);
			
		} catch (Exception e) {
				throw new RazorpayException(e.getMessage());
		}
	}
	@GetMapping("/payments")
	public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id")String paymentId,
			@RequestParam(name="order_id")Long orderId) throws OrderException, RazorpayException{
		Order order = orderService.findOrderById(orderId);
		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
		try {
			Payment payment = razorpay.payments.fetch(paymentId);
			if(payment.get("status").equals("Captured")) {
				order.getPaymentDetails().setPaymentId(paymentId);
				order.getPaymentDetails().setStatus("Completed");
				order.setOrderStatus("Placed");
				orderRepository.save(order);
			}
			ApiResponse res = new ApiResponse();
			res.setMessage("Order is Placed✅");
			res.setStatus(true);
			return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
		} catch (Exception e) {
			throw new RazorpayException(e.getMessage());
		}
	}
}
















