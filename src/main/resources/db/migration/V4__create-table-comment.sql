create table comments(

       id bigint not null auto_increment,
       user_id bigint not null,
       task_id bigint not null,
       comment varchar(800) not null,
       date datetime not null,

       primary key(id),
       constraint fk_comment_user_id foreign key(user_id) references users(id),
       constraint fk_comment_task_id foreign key(task_id) references tasks(id)
);

