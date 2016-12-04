Was coded in Java with Jersey.
Tested on Windows 7 64-bit Eclipse Java EE IDE Neon - Dynamic Web Project
MySQL Server
Apache Tomcat 8.5

The Dependencies are in the pom.xml file:

<dependencies>
  	<dependency>
  		<groupId>asm</groupId>
  		<artifactId>asm</artifactId>
  		<version>3.3.1</version>
  	</dependency>
  	<dependency>
  		<groupId>com.sun.jersey</groupId>
  		<artifactId>jersey-bundle</artifactId>
  		<version>1.19</version>
  	</dependency>
  	<dependency>
  		<groupId>org.json</groupId>
  		<artifactId>json</artifactId>
  		<version>20140107</version>
  	</dependency>
  	<dependency>
  		<groupId>com.sun.jersey</groupId>
  		<artifactId>jersey-server</artifactId>
  		<version>1.19</version>
  	</dependency>
  	<dependency>
  		<groupId>com.sun.jersey</groupId>
  		<artifactId>jersey-core</artifactId>
  		<version>1.19</version>
  	</dependency>
  	<dependency>
    	<groupId>com.google.code.gson</groupId>
    	<artifactId>gson</artifactId>
    	<version>2.6.2</version>
	</dependency>
	<dependency>
		<!-- jsoup HTML parser library @ http://jsoup.org/ -->
		<groupId>org.jsoup</groupId>
		<artifactId>jsoup</artifactId>
		<version>1.10.1</version>
	</dependency>
	<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
	<dependency>
	    <groupId>mysql</groupId>
	    <artifactId>mysql-connector-java</artifactId>
	    <version>5.1.6</version>
	</dependency>
  </dependencies>

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
