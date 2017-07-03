package pazeto.alertaanimal.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name="Alert")
public class Photo {

	public Photo(){}

//	public Photo(Animal animal, User user, double lat, double lng, String desc, boolean isAnonymous){
//		this.animal = animal;
//		this.user = user;
//		this.lat = lat;
//		this.lng = lng;
//		this.desc = desc;
//		this.isAnonymous = isAnonymous;
//	}

	@Id
	@Column(name="alertId")
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userId", nullable = false)
	@JsonBackReference
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="alertId", nullable = false)
	private Alert alert;

	@Column(name="description")
    private String desc;

//	@Column(name="lat", nullable = false)
//	private double lat;
//
//	@Column(name="lng", nullable = false)
//	private double lng;

//	@Column(name="isAnonymous", nullable = false)
//	@JsonProperty(value="isAnonymous")
//	private boolean isAnonymous;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
