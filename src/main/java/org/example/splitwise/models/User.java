package org.example.splitwise.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "users")
public class User extends BaseModel {
    private String name;
    private String phoneNumber;
    private String password;
}
