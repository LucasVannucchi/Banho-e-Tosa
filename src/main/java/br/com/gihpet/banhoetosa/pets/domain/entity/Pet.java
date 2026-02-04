package br.com.gihpet.banhoetosa.pets.domain.entity;

import br.com.gihpet.banhoetosa.common.domain.entity.History;
import br.com.gihpet.banhoetosa.pets.domain.enums.AnimalBreed;
import br.com.gihpet.banhoetosa.pets.domain.enums.Bearing;
import br.com.gihpet.banhoetosa.pets.domain.enums.TypeAnimal;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tb_pet")
public class Pet extends History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String namePet;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private TypeAnimal typeAnimal;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private AnimalBreed breed;

    private String color;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Bearing bearing = Bearing.MEDIUM;

    @Column
    private Double weight;

    private Boolean haveAllergy = false;

    @Column(length = 255)
    private String notes = "N/A";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNamePet() { return namePet; }
    public void setNamePet(String namePet) { this.namePet = namePet; }

    public TypeAnimal getTypeAnimal() { return typeAnimal; }
    public void setTypeAnimal(TypeAnimal typeAnimal) { this.typeAnimal = typeAnimal; }

    public AnimalBreed getBreed() { return breed; }
    public void setBreed(AnimalBreed breed) { this.breed = breed; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Bearing getBearing() { return bearing; }
    public void setBearing(Bearing bearing) { this.bearing = bearing; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Boolean getHaveAllergy() { return haveAllergy; }
    public void setHaveAllergy(Boolean haveAllergy) { this.haveAllergy = haveAllergy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}