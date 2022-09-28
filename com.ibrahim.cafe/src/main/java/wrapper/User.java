package wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

	private Integer id;
	private String name;
	private String email;
	private String phone;
	
	private String status;

	public User(Integer id, String name, String email, String phone, String status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.status = status;
	}
	
	
}
