package pazeto.alertaanimal.model;

import pazeto.alertaanimal.DTO.Animal;

import java.io.Serializable;

/**
 * Created by pazeto on 5/16/17.
 */

public class FilterObject implements Serializable{

    float range;
    Animal animal;
    float lat;
    float lng;


    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
