alter table Resource drop column volumeId, drop column instanceId, drop column availabilityZone;
alter table Resource add column resource_type varchar(255);
alter table Resource add index Resource_Type (resource_type);
create table VirtualCloudGatewayResource (id varchar(255) not null, availabilityZone varchar(255), instanceId varchar(255), volumeId varchar(255), primary key (id)) ENGINE=InnoDB;
create table PhysicalCloudGatewayResource (id varchar(255) not null, primary key(id)) ENGINE=InnoDB;
