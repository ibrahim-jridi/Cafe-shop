package jwt;

import java.util.ArrayList;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import model.User;
import serviceImpl.UserServiceImpl;
@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(CustomerUserDetailsService.class);
	
	@Autowired
	UserDao userDao;
	
	private User userDetails;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		logger.info("loadUserByUsername {}",username);
		userDetails = userDao.findByEmail(username);
		if (!Objects.isNull(userDetails)) {
			return new org.springframework.security.core.userdetails.User(userDetails.getEmail(),userDetails.getPassword(),new ArrayList<>());
			
		}else {
			throw new UsernameNotFoundException("User is not found");
		}
	}
public User getUserDetail() {
	return userDetails; 
}
	
}
