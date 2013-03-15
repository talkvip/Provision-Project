create table Capability (id varchar(255) not null, name varchar(255), description varchar(4096), uri varchar(255), primary key (id)) ENGINE=InnoDB;
alter table Capability add constraint Capability_name_unique UNIQUE (name);
create table Capability_Product (capability_id varchar(255), product_id varchar(255)) ENGINE=InnoDB;
alter table Capability_Product add constraint Capability_Product_Capability_fk foreign key (capability_id) references Capability (id);
alter table Capability_Product add constraint Capability_Product_Product_fk foreign key (product_id) references Product (id);
