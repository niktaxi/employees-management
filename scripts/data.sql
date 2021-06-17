insert into Employee values(1, 'Greg', sysdate, null);
insert into Employee values(2, 'Oleg', sysdate, 1);
insert into Employee values(3, 'Pete', sysdate, 2);
insert into Employee values(4, 'Paul', sysdate, 1);
insert into Employee values(5, 'Aura', sysdate, 1);
insert into Employee values(6, 'Phil', sysdate, 1);

insert into Attribute values (1, 'Height', 'Tall');
insert into Attribute values (2, 'Height', 'Short');
insert into Attribute values (3, 'Height', 'Medium');
insert into Attribute values (4, 'Height', 'Short');

insert into Attribute values (5, 'Weight', 'Medium');
insert into Attribute values (6, 'Weight', 'Thin');
insert into Attribute values (7, 'Weight', 'Heavy');

insert into EmployeeAttribute values (1, 1);
insert into EmployeeAttribute values (2, 2);
insert into EmployeeAttribute values (3, 3);
insert into EmployeeAttribute values (4, 4);

insert into EmployeeAttribute values (6, 7);
insert into EmployeeAttribute values (2, 6);
insert into EmployeeAttribute values (1, 5);