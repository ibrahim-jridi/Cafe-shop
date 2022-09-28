package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import model.User;

public interface UserDao extends JpaRepository<User, Integer> {
	User findByEmail(@Param("email") String email);
	List<wrapper.User> getAllUser();
	List<String> getAdmins();
	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String status,@Param("id") Integer id);
	User findByEmail1(String email);
}
