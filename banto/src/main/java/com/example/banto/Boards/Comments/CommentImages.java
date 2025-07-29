package com.example.banto.Boards.Comments;

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
public class CommentImages {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMG_URL")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="COMMENT_PK")
    private Comments comment;

    public static CommentImages toEntity(Comments comment, String imgUrl){
        return CommentImages.builder()
            .comment(comment)
            .imgUrl(imgUrl)
            .build();
    }
}
