package id.putra.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import io.quarkiverse.renarde.security.RenardeUser;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "users")
@Entity
public class User implements RenardeUser, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    public String username;

    @Column(nullable = false)
    public String password;

    public String firstname;

    public String lastname;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    public Boolean isActive = true;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    public List<String> roles;

    @OneToMany(mappedBy = "user")
    public List<Topic> topics;

    @OneToMany(mappedBy = "user")
    public List<Vote> votes;


    @Override
    public Set<String> roles() {
        return new HashSet<>(this.roles);
    }

    @Override
    public String userId() {
        return String.valueOf(this.id);
    }

    @Override
    public boolean registered() {
        return true;
    }
}
