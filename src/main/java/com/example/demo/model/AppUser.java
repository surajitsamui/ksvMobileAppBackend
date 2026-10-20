package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private Role role;
    @Column(nullable=false)
    private String fullName;
    private String mobile;
    @OneToOne
    @JoinColumn(name="devotee_id", unique=true)
    private Devotee devotee;

    protected AppUser() {}
    public AppUser(String email, String passwordHash, Role role, String fullName, String mobile, Devotee devotee) {
        this.email=email; this.passwordHash=passwordHash; this.role=role; this.fullName=fullName; this.mobile=mobile; this.devotee=devotee;
    }
    public Long getId(){return id;} public String getEmail(){return email;}
    public String getPasswordHash(){return passwordHash;} public Role getRole(){return role;}
    public String getFullName(){return fullName;} public String getMobile(){return mobile;}
    public Devotee getDevotee(){return devotee;}
}
