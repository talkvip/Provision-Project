alter table CertificateSubject add column CA bit(1);
alter table CertificateSubject add column pathlen integer not null;
update CertificateSubject set CA=true, pathlen=-1;