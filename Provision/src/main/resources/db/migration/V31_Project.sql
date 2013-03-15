delete from Product_Metadata;
delete from Capability_Product;
delete from ResourcePoolItem;
delete from VirtualResource;
delete from Resource;
delete from Product;
create table Project (id varchar(255) not null, name varchar(255),accessKey varchar(255),secretKey varchar(255),endPointURL varchar(255), primary key (id)) ENGINE = InnoDB;
alter table Product add column (project_id varchar(255) not null);
alter table Product add index Product_project_id_index (project_id), add constraint Product_project_id_fk foreign key (project_id) references Project (id);




