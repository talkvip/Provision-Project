delete from Resource;
delete from PhysicalResource;
delete from VirtualResource;

delete from Product;
delete from PhysicalProduct;
delete from VirtualProduct;

alter table VirtualProduct drop column provisioningStrategy;
alter table Product add column provisioningStrategy varchar(255);