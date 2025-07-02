package id.putra.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    public String description;

    public Date datetime;

    @OneToMany(mappedBy = "category")
    public List<Topic> topics;

//    public int sortOrder;
}
