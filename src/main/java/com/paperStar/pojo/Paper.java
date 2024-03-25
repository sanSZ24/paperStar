package com.paperStar.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "paper")
public class Paper {
    @Id
    private int paperId;

    private int userId;//问卷所属用户的id，外键
    private String title;
    private int status;

    private String CreateTime;//创建时间
    private String startTime;//开始时间
    private String endTime;//截止时间
}
