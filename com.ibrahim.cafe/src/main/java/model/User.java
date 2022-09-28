package model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@NamedQuery(name = "User.findByEmail",query = "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUser",query = "select new wrapper.User(u.id,u.name,u.email,u.phone,u.status) from User u where u.role = 'user'")
@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where u.id=:id ")
@NamedQuery(name = "User.getAdmins",query = "select u.email from User u where u.role = 'admin'")



@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")

public class User implements Serializable {

		private static final long serialVersion = 1L;
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id")
		private Integer id;
		
		@Column(name = "name")
		private String name;
		
		@Column(name = "phone")
		private String phone;
		
		@Column(name = "email")
		private String email;
		
		@Column(name = "password")
		private String password;
		
		@Column(name = "status")
		private String status;
		
		@Column(name = "role")
		private String role;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
}
