package com.bloodlink.entities;

import com.bloodlink.entities.enums.OrganizationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String address;
    private String phone;
    private Integer hoursFrom;
    private Integer hoursTo;
    private Integer minutesFrom;
    private Integer minutesTo;

    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    @ToString.Exclude
    @OneToMany(mappedBy = "organization")
    private List<User> members;
}
