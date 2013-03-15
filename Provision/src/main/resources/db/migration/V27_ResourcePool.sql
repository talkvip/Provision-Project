create table ResourcePoolItem (id varchar(255) not null, product_id varchar(255), resource_id varchar(255), primary key (id)) ENGINE=InnoDB;
alter table ResourcePoolItem add index ResourcePoolItem_product_id_index (product_id), add constraint ResourcePoolItem_product_id_fk foreign key (product_id) references Product (id);
alter table ResourcePoolItem add index ResourcePoolItem_resource_id_index (resource_id), add constraint ResourcePoolItem_resource_id_fk foreign key (resource_id) references Resource (id);
