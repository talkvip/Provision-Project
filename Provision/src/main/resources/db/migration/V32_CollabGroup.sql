create table CollaborationGroup (id varchar(255) not null, user_id varchar(255) not null, resource_id varchar(255), primary key (id)) ENGINE=InnoDB;
create table CollaborationGroup_User (collaborationGroup_id varchar(255) not null, user_id varchar(255) not null) ENGINE = InnoDB;
alter table CollaborationGroup add index CollaborationGroup_user_id_index (user_id), add constraint CollaborationGroup_user_id_fk foreign key (user_id) references User (id);
alter table CollaborationGroup add index CollaborationGroup_resource_id_index (resource_id), add constraint CollaborationGroup_resource_id_fk foreign key (resource_id) references Resource (id);
alter table CollaborationGroup_User add index CollaborationGroup_User_collaborationGroup_id_index (collaborationGroup_id), add constraint CollaborationGroup_User_collaborationGroup_id_fk foreign key (collaborationGroup_id) references CollaborationGroup (id);
alter table CollaborationGroup_User add index CollaborationGroup_User_user_id_index (user_id), add constraint CollaborationGroup_User_user_id_index foreign key (user_id) references User (id);
