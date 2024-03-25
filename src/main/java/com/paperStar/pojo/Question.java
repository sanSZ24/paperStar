package com.paperStar.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Table(name = "question")
public class Question {
    @Id
    private int questionId;

    private int paperId;

    private String questionTitle;
    private int questionType;
    private String option;
}
