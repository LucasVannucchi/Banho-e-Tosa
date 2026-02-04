package br.com.gihpet.banhoetosa.addresses.domain.entity;

import br.com.gihpet.banhoetosa.addresses.domain.enums.TypeHousing;
import br.com.gihpet.banhoetosa.common.domain.entity.History;
import br.com.gihpet.banhoetosa.users.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_address")
public class Address extends History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String street;

    @Column(length = 10)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private TypeHousing typeHousing = TypeHousing.HOUSE;

    @Column(length = 50)
    private String complement;

    @Column(length = 60, nullable = false)
    private String neighborhood;

    @Column(length = 60, nullable = false)
    private String city;

    @Column(length = 2, nullable = false)
    private String state;

    @Column(length = 10, nullable = false)
    private String zipCode;

    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public TypeHousing getTypeHousing() { return typeHousing; }
    public void setTypeHousing(TypeHousing typeHousing) { this.typeHousing = typeHousing; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}