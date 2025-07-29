package com.example.banto.Boards.Qnas;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Options.Options;
import com.example.banto.Users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QNAs {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="Q_CONTENT", nullable=false)
    private String qContent;
    
    @Column(name="A_CONTENT")
    private String aContent;
    
    @Column(name = "Q_WRITE_DATE", nullable=false)
    private LocalDateTime qWriteDate;
    
    @Column(name="A_WRITE_DATE")
    private LocalDateTime aWriteDate;

    @ManyToOne
    @JoinColumn(name="WRITER_PK")
    private Users user;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="OPTION_PK")
    private Options option;

    @OneToMany(mappedBy="qna")
    private List<QNAImages> qnaImages;

    public static QNAs toEntity(Users user, Options option, String qContent){
        return QNAs.builder()
            .user(user)
            .option(option)
            .qContent(qContent)
            .qWriteDate(LocalDateTime.now())
            .build();
    }
}
