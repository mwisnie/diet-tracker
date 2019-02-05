package mw.project.diettracker.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String password;
    private String email;

}
