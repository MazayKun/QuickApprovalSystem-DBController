create table qas.process_type
(
    id_process_type serial       not null
        constraint process_type_pk
            primary key,
    name            varchar(100) not null,
    description     varchar(300),
    time_to_do      integer default 1
);

comment on table qas.process_type is 'variable type for process';

comment on column qas.process_type.description is 'описание типа процесса';

comment on column qas.process_type.time_to_do is 'количество часов для выполнения процесса';

alter table qas.process_type
    owner to docker;

create unique index process_type_id_process_type_uindex
    on qas.process_type (id_process_type);

create table qas.role_qas
(
    id_role serial not null
        constraint role_pk
            primary key,
    name    varchar(100)
);

comment on table qas.role_qas is 'роль пользователей. Уникальная группа пользователей. Пользователь может входит в несколько групп';

alter table qas.role_qas
    owner to docker;

create unique index role_role_id_uindex
    on qas.role_qas (id_role);

create table qas.user_qas
(
    id_user          serial       not null
        constraint user_pk
            primary key,
    fio              varchar(200) not null,
    login            varchar(100) not null,
    password         varchar(100) not null,
    email            varchar(100),
    telegram_chat_id integer
);

comment on table qas.user_qas is 'пользователи системы';

comment on column qas.user_qas.telegram_chat_id is 'Уникальный идентификатор из чата бота телеграмма, который присваивается пользоватлею';

alter table qas.user_qas
    owner to docker;

create unique index user_id_user_uindex
    on qas.user_qas (id_user);

create table qas.user_role
(
    user_role_id serial  not null
        constraint user_role_pk
            primary key,
    user_id      integer not null
        constraint user_role_user_qas_id_user_fk
            references qas.user_qas,
    role_id      integer
        constraint user_role_role_id_fk
            references qas.role_qas
);

comment on table qas.user_role is 'вхождение пользователей в группы';

alter table qas.user_role
    owner to docker;

create unique index user_role_user_role_id_uindex
    on qas.user_role (user_role_id);

create table qas.status
(
    id_status serial not null
        constraint status_pk
            primary key,
    name      varchar(100)
);

alter table qas.status
    owner to docker;

create table qas.process
(
    id_process        serial    not null
        constraint process_pk
            primary key,
    process_type_id   integer   not null
        constraint process_process_type_id_process_type_fk
            references qas.process_type,
    name              varchar(100),
    description       varchar(300),
    user_start_id     integer
        constraint process_user_qas_id_user_fk
            references qas.user_qas,
    date_start        timestamp not null,
    date_end_planning timestamp not null,
    date_end_fact     timestamp,
    status_id         integer
        constraint process_status_id_status_fk
            references qas.status
);

comment on table qas.process is 'table for agreement process';

comment on column qas.process.name is 'Название процесса';

comment on column qas.process.description is 'Поле для описания от заказчика-пользователя';

comment on column qas.process.user_start_id is 'Пользователь-заказчик который запустил процесс';

comment on column qas.process.date_start is 'дата старта процесса';

comment on column qas.process.date_end_planning is 'Плановая дата завершения процесса. Это дедлайн';

comment on column qas.process.date_end_fact is 'Фактическое время завершения процесса';

alter table qas.process
    owner to docker;

create unique index process_id_process_uindex
    on qas.process (id_process);

create table qas.task
(
    id_task           serial    not null
        constraint task_pk
            primary key,
    process_id        integer
        constraint task_process_id_process_fk
            references qas.process,
    user_performer_id integer
        constraint task_user_qas_id_user_fk
            references qas.user_qas,
    role_performer_id integer
        constraint task_role_qas_id_role_fk
            references qas.role_qas,
    date_start        timestamp not null,
    date_end_planning timestamp,
    date_end_fact     timestamp,
    status_id         integer
        constraint task_status_id_status_fk
            references qas.status
);

comment on table qas.task is 'список всех задач';

comment on column qas.task.user_performer_id is 'пользователь, который назначен на задачу. Исполнитель';

comment on column qas.task.role_performer_id is 'коллектив, назначенный на задачу';

comment on column qas.task.date_start is 'дата и время начала задачи';

comment on column qas.task.date_end_planning is 'дата и время дедлайна по задаче';

comment on column qas.task.date_end_fact is 'дата фактического исполнения задачи';

alter table qas.task
    owner to docker;

create unique index task_id_task_uindex
    on qas.task (id_task);

create unique index status_id_status_uindex
    on qas.status (id_status);

create table qas.process_stage
(
    id_process_stage serial not null
        constraint process_stage_pk
            primary key,
    process_type_id  integer
        constraint process_stage_process_type_id_process_type_fk
            references qas.process_type,
    stage            integer,
    role_id          integer
        constraint process_stage_role_qas_id_role_fk
            references qas.role_qas
);

comment on column qas.process_stage.stage is 'Порядок выполнение для задачи';

alter table qas.process_stage
    owner to docker;

create unique index process_stage_id_process_stage_uindex
    on qas.process_stage (id_process_stage);