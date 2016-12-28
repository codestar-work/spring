package web;
import javax.persistence.*;

@Entity @Table(name="member")
public class Member {
	@Id
	public long code;
	public String email;
	public String password;
	public String name;
}
