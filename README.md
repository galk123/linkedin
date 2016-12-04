* MySQL Database initial setup:

create database Linkedin;
create table LinkedinProfiles
(
	Id int not null AUTO_INCREMENT,
	Url varchar(255) not null,
	ProfileName varchar(255) not null,
	CurrentTitle varchar(255),
	CurrentPosition varchar(255),
	Summary varchar(500),
	Experience varchar(5000),
	Education varchar(5000),
	Score int not null,
	PRIMARY KEY (Id),
	UNIQUE(Url)
);
create table LinkedinProfileSkills
(
	Id int not null AUTO_INCREMENT,
	SkillName varchar(255) not null,
	ProfileId int not null,
	primary key (Id),
	foreign key (ProfileId) references LinkedinProfiles(Id)
);

* PUT HTTP Request
Add a profile:
endpoint: http://localhost:8080/LinkedinApp/api/add/url/
body: [linkedin profile public url]

* GET HTTP Request:
Available endpoints:

http://localhost:8080/LinkedinApp/api/search/name/[name]
http://localhost:8080/LinkedinApp/api/search/title/[title]
http://localhost:8080/LinkedinApp/api/search/position/[position]
http://localhost:8080/LinkedinApp/api/search/summary/[summary]
http://localhost:8080/LinkedinApp/api/search/skills/[skill1],[skill2],...
