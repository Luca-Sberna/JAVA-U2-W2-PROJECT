package JAVAU2W2PROJECT.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue
	private String username;
	private String email;
	private String name;
	private String surname;

}
