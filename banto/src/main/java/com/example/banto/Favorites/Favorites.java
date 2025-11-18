package com.example.banto.Favorites;

import com.example.banto.Items.Items;
import com.example.banto.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorites {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_PK")
    private Users user;

    @ManyToOne
    @JoinColumn(name="ITEM_PK")
    private Items item;
}
