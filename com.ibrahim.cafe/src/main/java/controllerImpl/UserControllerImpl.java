package controllerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import constants.cafeConstant;
import controller.UserController;
import wrapper.User;
import service.UserService;
import utils.cafeUtils;
@RestController
public class UserControllerImpl implements UserController {
	
	@Autowired
	UserService userService;
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return userService.signUp(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return userService.login(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW,HttpStatus.INTERNAL_SERVER_ERROR);

	}
	@Override
	public ResponseEntity<List<wrapper.User>> getAllUser() {
		// TODO Auto-generated method stub
		try {
			return userService.getAllUser();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<List<User>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return userService.update(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> checkToken() {
		// TODO Auto-generated method stub
		try {
			return userService.checkToken();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return userService.changePassword(requestMap);
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
			return userService.forgetPassword(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
