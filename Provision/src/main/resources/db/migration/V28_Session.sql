create table Session (id varchar(64) not null, user_id varchar(255) not null, expiresAt datetime, primary key (id)) ENGINE=InnoDB;
alter table Session add index Session_user_id_index (user_id), add constraint Session_user_id_fk foreign key (user_id) references User (id);
