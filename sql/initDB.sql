/*
	create a sqlite3 database named VIPManager.db,
	then feed it with this script
*/
create table cards(
	ID int primary key not null, 
	name text not null, 
	idcard text, 
	phone text, 
	credit real default 0, 
	regtime text);

create table transhistory(
	ID int not null, 
	creditchange real,
	time text,
	comment text);

create table loginmsg(
	username text primary key not null,
	passwordhash text not null,
	salt text not null
);

/*
	default user: admin
	default pass: admin (binary SHA256 salted result)
	default salt: defaultsalt (ascii to binary);
*/
insert into loginmsg(username, passwordhash, salt)
	values("admin", "59cf25ae45a72ecd360ad5cf0bb722670b35ad59c7969632c10e05b9f010dd32", "64656661756c7473616c74");