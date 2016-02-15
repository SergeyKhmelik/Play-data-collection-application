package models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_user")
    public int idUser;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String password;

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", username=" + username
				+ ", password=" + password + "]";
	}
    
    
}
