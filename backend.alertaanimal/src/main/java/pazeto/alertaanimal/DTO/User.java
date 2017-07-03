package pazeto.alertaanimal.DTO;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="User")
public class User {

	public User(){}

	@Id
	@Column(name="userId")
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@Column(name="name")
    private String name;

	@Column(name="lastName")
	private String lastName;

	@Column(name="facebookId", unique = true)
	private Long facebookId;

	private String email;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonManagedReference
	private Set<Alert> alerts = new HashSet<>(0);

	@Column
	@Type(type="timestamp")
	private Date lastSync;

	public Set<Alert> getAlerts() {
		return this.alerts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(Long facebookId) {
		this.facebookId = facebookId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAlerts(Set<Alert> alerts) {
		this.alerts = alerts;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}

	public enum Field {
		name,
		lastName,
		facebookId,
		email,
		lastSync
	}

}
