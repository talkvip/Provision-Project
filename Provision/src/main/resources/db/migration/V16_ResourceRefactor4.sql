alter table Product drop column serial;
alter table PhysicalResource add column serialKey varchar(255);
delete from Resource;
delete from Capability_Product;
delete from Product;
