package id.putra.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "votes")
public class Vote extends PanacheEntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @ManyToOne
    @JoinColumn(name = "post_id")
    public Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Column(name = "vote_type")
    public Integer voteType;
}
