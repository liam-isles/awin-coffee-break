package com.awin.coffeebreak.entity;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Configuration
@Table(name = "staff_member")
public class StaffMember {

    @Id
    Integer id;

    @Column
    String name;

    @Column
    String email;

    @OneToMany
    List<CoffeeBreakPreference> coffeeBreakPreferences = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public List<CoffeeBreakPreference> getCoffeeBreakPreferences() {
        return coffeeBreakPreferences;
    }

    public void setCoffeeBreakPreferences(
          final List<CoffeeBreakPreference> coffeeBreakPreferences
    ) {
        this.coffeeBreakPreferences = coffeeBreakPreferences;
    }
}
