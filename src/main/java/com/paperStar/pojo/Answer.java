package com.paperStar.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "answer")
public class Answer {
    @Id
    private int answerId;
    private int questionId;
    private int questionType;
    private String questionOption;
}
