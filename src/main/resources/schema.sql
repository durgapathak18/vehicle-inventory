drop table if exists vehicle;

create table vehicle
(
   id integer not null AUTO_INCREMENT,
   make varchar(20) not null,
   name varchar(20) not null,
   VEHICLE_TYPE varchar(20) not null,
   model varchar(20) not null,
   color varchar(20) not null,
   PRIMARY KEY (id)
);