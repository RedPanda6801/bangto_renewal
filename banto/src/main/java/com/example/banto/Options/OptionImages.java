package com.example.banto.Options;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionImages {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="IMG")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="OPTION_PK")
    private Options option;

    public static OptionImages toEntity(Options option, String imgUrl){
        return OptionImages.builder()
            .option(option)
            .imgUrl(imgUrl)
            .build();
    }
}
