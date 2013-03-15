create table ProductOrder (id varchar(255) not null, product_id varchar(255) not null, editionCode varchar(255), pricingDuration varchar(255), creator_id varchar(255), primary key(id)) ENGINE = InnoDB;
create table OrderItem (id varchar(255) not null, quantity varchar(255), unit varchar(255), productOrder_id varchar(255) not null, primary key(id)) ENGINE = InnoDB;
alter table OrderItem add index OrderItem_order_id_index (productOrder_id), add foreign key OrderItem_order_id_fk (productOrder_id) references ProductOrder (id);
alter table ProductOrder add index ProductOrder_user_id_index (creator_id), add foreign key ProductOrder_user_id_fk (creator_id) references User (id);
alter table ProductOrder add index ProducOrder_product_id_index (product_id), add foreign key ProductOrder_product_id_fk (product_id) references Product (id);

create table Resource_User (resource_id varchar(255) not null, user_id varchar(255) not null) ENGINE = InnoDB;
alter table Resource_User add index Resource_User_resource_id_index (resource_id), add foreign key Resource_User_resource_id (resource_id) references Resource (id);
alter table Resource_User add index Resource_User_user_id_index (user_id), add foreign key Resource_User_user_id (user_id) references User (id);
alter table Resource add column owner_id varchar(255);
alter table Resource add index Resource_owner_id_index (owner_id), add foreign key Resource_owner_id_fk (owner_id) references User (id);
alter table Resource add column productOrder_id varchar(255);
alter table Resource add index Resource_order_id_index (productOrder_id), add foreign key Resource_order_id_fk (productOrder_id) references ProductOrder (id);
