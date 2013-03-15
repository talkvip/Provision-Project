alter table Company drop column title;
alter table Company add column country varchar(255), add column email varchar(255), add column phoneNumber varchar(255), add column website varchar(255);
alter table User add column openId varchar(500);
