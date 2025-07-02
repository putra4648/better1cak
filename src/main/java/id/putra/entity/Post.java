package id.putra.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "posts")
public class Post extends PanacheEntityBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String content;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    public Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    public Post parent;

    @Column(name = "is_edited", columnDefinition = "boolean default true")
    public Boolean isEdited = true;
}
