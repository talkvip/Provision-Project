delete from Resource;
delete from PhysicalResource;
delete from VirtualResource;

delete from Product;
delete from PhysicalProduct;
delete from VirtualProduct;

alter table Product drop column provisioningStrategy;
alter table VirtualProduct add column provisioningStrategy varchar(255);