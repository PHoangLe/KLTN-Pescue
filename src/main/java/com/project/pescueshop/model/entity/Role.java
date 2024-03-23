package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ROLE")
@Entity
@Name(prefix = "ROLE")
public class Role {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String roleId;
    private String roleName;

    public Role(String roleName){
        this.roleName = roleName;
    }
}
