package main.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "pages")
@NoArgsConstructor
public class Page {

    @Id
    @Column(name = "page_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Page(Long id, Long owner, String text, String role) {
        this.id = id;
        this.owner = owner;
        this.text = text;
        this.role = role;
    }
    @Column(name = "owner")
    private Long owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "roles")
    private String role;

}
