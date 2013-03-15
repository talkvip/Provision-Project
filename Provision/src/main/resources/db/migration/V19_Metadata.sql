create table Metadata (id varchar(255) not null, name varchar(255), value varchar(4000), primary key (id)) ENGINE=InnoDB;
create table Product_Metadata (product_id varchar(255) not null, metadata_id varchar(255) not null) ENGINE = InnoDB;
alter table Product_Metadata add index Product_Metadata_product_id_index (product_id), add constraint Product_Metadata_product_id_fk foreign key (product_id) references Product (id);
alter table Product_Metadata add index Product_Metadata_metadata_id_index (metadata_id), add constraint Product_Metadata_metadata_id_fk foreign key (metadata_id) references Metadata (id);
