package id.putra.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    public String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    @OneToMany(mappedBy = "topic")
    public List<Post> posts;

    @Column(name = "is_locked", columnDefinition = "boolean default false")
    public Boolean isLocked = false;

    @Column(name = "is_pinned", columnDefinition = "boolean default false")
    public Boolean isPinned = false;

    @Column(name = "view_count")
    public Long viewCount;

}
