package serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import constants.cafeConstant;
import dao.UserDao;
import jwt.CustomerUserDetailsService;
import jwt.JwtUtil;
import jwt.jwtFilter;
import lombok.extern.slf4j.Slf4j;
import model.User;
import service.UserService;
import utils.EmailUtils;
import utils.cafeUtils;
@Slf4j
@Service
public class UserServiceImpl implements UserService{
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	UserDao userDao;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	CustomerUserDetailsService CustomerUserDetailsService;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	jwtFilter jwtFilter;
	@Autowired
	EmailUtils emailUtils;
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		logger.info("Inside signup{}",requestMap);
		try {
		if (signUpMapValidation(requestMap)) {
			User user = userDao.findByEmail(requestMap.get("email"));
			if (Objects.isNull(user)) {
				userDao.save(getUser(requestMap));
				return cafeUtils.getResponseEntity("User Registered Successfully", HttpStatus.OK);
			} else {
				return cafeUtils.getResponseEntity("Email is already exists",HttpStatus.BAD_REQUEST);
			}
		} else {
			return cafeUtils.getResponseEntity(cafeConstant.DII, HttpStatus.BAD_REQUEST);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	private boolean signUpMapValidation(Map<String,String> requestMap) {
		if(requestMap.containsKey("name") && requestMap.containsKey("phone") && 
		requestMap.containsKey("email") && requestMap.containsKey("password") ) {
			return true;
		}
		return false;
	}
	private User getUser(Map<String,String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setPhone(requestMap.get("phone"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
		
	}
	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		logger.info("Inside login{}",requestMap);
		try {
			org.springframework.security.core.Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password")));
			if (auth.isAuthenticated()) {
				if (CustomerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateToken(CustomerUserDetailsService.getUserDetail().getEmail(),CustomerUserDetailsService.getUserDetail().getRole() +"\"}"),HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("{\"message\":\""+"wait the admin to acceprt your request"+"\"}",HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("{}",e);
		}
		return new ResponseEntity<String>("{\"message\":\""+"bad credentials"+"\"}",HttpStatus.BAD_REQUEST);

	}
	@Override
	public ResponseEntity<List<wrapper.User>> getAllUser() {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isUser()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					sendMailToAdmins(requestMap.get("status"),optional.get().getEmail(),userDao.getAdmins());					return cafeUtils.getResponseEntity("user status updated successfully", HttpStatus.OK);
				} else {
					return cafeUtils.getResponseEntity("there's no user with this id", HttpStatus.OK);
				}
			} else {
				return cafeUtils.getResponseEntity(cafeConstant.UA,HttpStatus.UNAUTHORIZED );
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	private void sendMailToAdmins(String status, String user, List<String> admins) {
		// TODO Auto-generated method stub
		admins.remove(jwtFilter.getCurrentUser() );
		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendMail(jwtFilter.getCurrentUser(),"account update approved","USER: "+user+"\n is approved by Admin : "+jwtFilter.getCurrentUser(),admins);
		}else {
			emailUtils.sendMail(jwtFilter.getCurrentUser(),"account update not approved","USER: "+user+"\n is disapproved by Admin : "+jwtFilter.getCurrentUser(),admins);

		}
	}
	@Override
	public ResponseEntity<String> checkToken() {
		// TODO Auto-generated method stub
		return cafeUtils.getResponseEntity("true", HttpStatus.OK);
	}
	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			User userObj = userDao.findByEmail1(jwtFilter.getCurrentUser());
			if (!userObj.equals(null)) {
				if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
					userObj.getPassword().equals(requestMap.get("newPassword"));
					userDao.save(userObj);
					return cafeUtils.getResponseEntity("Password Successfully Updated", HttpStatus.OK);
				}
				return cafeUtils.getResponseEntity("Incorrect Password", HttpStatus.BAD_REQUEST);

			}
			return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);

		
	}
	@Override
	public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			User user = userDao.findByEmail1(requestMap.get("email"));
			if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
				emailUtils.forgetMail(user.getEmail(), "your cerdentilals", user.getPassword());
			}
			return cafeUtils.getResponseEntity("we sent the cerdentials to your mail", HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	
	
}
