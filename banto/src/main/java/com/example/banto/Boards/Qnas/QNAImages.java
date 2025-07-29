package com.example.banto.Boards.Qnas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QNAImages {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="IMG_URL")
    String imgUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="QNA_PK")
    private QNAs qna;

    public static QNAImages toEntity(QNAs qna, String imgUrl){
        return QNAImages.builder()
            .qna(qna)
            .imgUrl(imgUrl)
            .build();
    }
}
