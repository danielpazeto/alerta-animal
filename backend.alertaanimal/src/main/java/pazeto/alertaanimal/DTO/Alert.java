package pazeto.alertaanimal.DTO;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
@Table(name="Alert")
public class Alert {

	public Alert(){}

	public Alert(Animal animal, User user, double lat, double lng, String desc, boolean isAnonymous){
		this.animal = animal;
		this.user = user;
		this.lat = lat;
		this.lng = lng;
		this.desc = desc;
		this.isAnonymous = isAnonymous;
	}

	@Id
	@Column(name="alertId")
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userId", nullable = false)
	@JsonBackReference
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="animalId", nullable = false)
	private Animal animal;

	@Column(name="description")
    private String desc;

	@Column(name="lat", nullable = false)
	private double lat;

	@Column(name="lng", nullable = false)
	private double lng;

	@Column(name="isAnonymous", nullable = false)
	@JsonProperty(value="isAnonymous")
	private boolean isAnonymous;

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

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean anonymous) {
		isAnonymous = anonymous;
	}
}
