create table ActivationSerial (id varchar(255) not null, serial varchar(255) unique, server varchar(255), primary key (id)) Engine=InnoDB;
ALTER TABLE ActivationSerial ADD UNIQUE (serial);
