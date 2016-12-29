package web;
import javax.persistence.*;

@Entity @Table(name="topic")
public class Topic {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	long code;
	String topic;
	String detail;
	String photo;
	long user;
}
