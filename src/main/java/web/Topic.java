package web;
import javax.persistence.*;

@Entity @Table(name="topic")
public class Topic {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	long code;
	String title;
	String detail;
	String photo;
	@Column(name="user")
	long member;
}
