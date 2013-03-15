create table User (id varchar(255) not null, name varchar(255), certificate_id varchar(255) not null, primary key (id)) ENGINE=InnoDB;
alter table User add index User_certificate_index (certificate_id), add constraint User_certificate_fk foreign key (certificate_id) references Certificate (id);
alter table User add constraint User_name_unique UNIQUE (name);
