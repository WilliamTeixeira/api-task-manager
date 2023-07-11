create table tasks(

    id bigint not null auto_increment,
    user_creator_id bigint not null,
    user_from_id bigint not null,
    user_to_id bigint not null,
    person_requesting_id bigint not null,
    title varchar(255),
    description varchar(800),
    status varchar(100) not null,
    created_at datetime not null,
    updated_at datetime,
    completed_at datetime,

    primary key(id),
    constraint fk_task_user_creator_id foreign key(user_creator_id) references users(id),
    constraint fk_task_user_from_id foreign key(user_from_id) references users(id),
    constraint fk_task_user_to_id foreign key(user_to_id) references users(id),
    constraint fk_task_person_requesting_id foreign key(person_requesting_id) references persons(id)
);