delete from User;
create table ClaimCheck (id varchar(255) not null, claimed bit(1) not null, user_id varchar(255) not null, createdOn DATETIME, primary key (id), unique (id)) ENGINE=InnoDB;
alter table ClaimCheck add index ClaimCheck_user_id_index (user_id), add constraint ClaimCheck_user_id_fk foreign key (user_id) references User (id);
alter table User add column verified bit(1) not null, add column email varchar(255), add column password varchar(255);
alter table User add index User_email_index (email);

