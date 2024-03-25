use paperstar;

create table user(
    id int primary key auto_increment,
    userName varchar(64) not null,
    password varchar(100) not null,
    email varchar(64),
    role varchar(64) default 'user',
    salt varchar(100),
    createTime varchar(64),
    lastLoginTime varchar(64),
    status int default '0'
);

create table paper(
    paperId int primary key auto_increment,
    userId int,
    title varchar(100),
    status int,
    createTime varchar(64),
    startTime varchar(64),
    endTime varchar(64),
    foreign key(userId) references user(id)
);


create table question(
    questionId int primary key auto_increment,
    paperId int,
    questionTitle varchar(100),
    questionType int,
    questionOption varchar(500),
    foreign key (paperId) references paper(paperId)
);


create table answer(
    answerId int primary key auto_increment,
    questionId int,
    questionType int,
    questionOption varchar(500),
    foreign key (questionId) references question(questionId)
)