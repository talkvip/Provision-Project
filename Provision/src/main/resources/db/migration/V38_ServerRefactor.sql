drop table Region_availabilityZones;
drop table Region;
drop table Project;
alter table VirtualResource drop column availabilityZone;
alter table VirtualResource change instanceId serverId varchar(255);