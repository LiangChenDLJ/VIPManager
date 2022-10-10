/*
	reset pass to default
	default user: admin
	default pass: admin (binary SHA256 salted result)
	default salt: defaultsalt (ascii to binary);
*/

delete from loginmsg;

insert into loginmsg(username, passwordhash, salt)
	values("admin", "59cf25ae45a72ecd360ad5cf0bb722670b35ad59c7969632c10e05b9f010dd32", "64656661756c7473616c74");