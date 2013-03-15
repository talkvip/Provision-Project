alter table Certificate add column role integer not null;
alter table Certificate add index Certificate_role_index (role);
update Certificate set role = 0;