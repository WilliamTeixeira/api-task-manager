create table status_histories(

       id bigint not null auto_increment,
       user_id bigint not null,
       task_id bigint not null,
       status_from varchar(100) not null,
       status_to varchar(100) not null,
       date datetime not null,

       primary key(id),
       constraint fk_status_histories_user_id foreign key(user_id) references users(id),
       constraint fk_status_histories_task_id foreign key(task_id) references tasks(id)
);



