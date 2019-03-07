package mw.project.diettracker.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    private String username;
    private String authority;

}
