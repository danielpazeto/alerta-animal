package pazeto.alertaanimal.DTO;

import javax.persistence.*;

@Entity
@Table(name="Animal")
public class Animal {

	public Animal(){}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="animalId")
	private long id;

	@Column(name="name")
    private String name;

	public Animal(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
