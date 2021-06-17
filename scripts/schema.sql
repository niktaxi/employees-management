CREATE TABLE Employee(
	ID NUMBER(4,0) NOT NULL PRIMARY KEY,
	name varchar(100) NOT NULL,
	hire_day DATE NOT NULL,
	supervisor_id NUMBER(4,0) NULL
    );
    
CREATE TABLE Attribute(
	ID NUMBER(4,0) NOT NULL PRIMARY KEY,
	name varchar(50) NOT NULL,
	value varchar(50) NOT NULL
);

CREATE TABLE EmployeeAttribute(
	EmployeeID NUMBER(4,0) NOT NULL,
	AttributeID NUMBER(4,0) NOT NULL,
    CONSTRAINT PK_EmployeeAttribute PRIMARY KEY ( EmployeeID,AttributeID)
);

ALTER TABLE Employee ADD  CONSTRAINT FK_Employee_Employee FOREIGN KEY(supervisor_id)
REFERENCES Employee (ID);

ALTER TABLE EmployeeAttribute  ADD  CONSTRAINT FK_EmployeeAttribute_Attribute FOREIGN KEY(AttributeID)
REFERENCES Attribute (ID);

ALTER TABLE EmployeeAttribute ADD  CONSTRAINT FK_EmployeeAttribute_Employee FOREIGN KEY(EmployeeID)
REFERENCES Employee (ID);

CREATE SEQUENCE employee_seq START WITH 6;

CREATE SEQUENCE attr_seq START WITH 7;