DECLARE 
    attr_id attribute.id%type; 
    emp_id employee.id%type; 
    CURSOR c_attribute IS 
        SELECT id FROM Attribute WHERE name='Weight';
    CURSOR c_emp IS
        SELECT E.id FROM Employee E
        MINUS 
        SELECT E.id FROM Employee E 
        INNER JOIN EmployeeAttribute EA 
        ON E.id=EA.employeeid 
        INNER JOIN Attribute A 
        ON A.id=EA.attributeid
        WHERE A.name='Weight' and A.value='Thin';
    
BEGIN

    OPEN c_attribute;
    FETCH c_attribute into attr_id; 
    
    IF c_attribute%notfound THEN
        SELECT attr_seq.nextval INTO attr_id FROM dual;
        
        INSERT INTO Attribute(id, name, value) VALUES(attr_id, 'Weight', 'Thin');
    ELSE
        UPDATE Attribute SET value='Thin' WHERE name='Weight';
    END IF;
    
    OPEN c_emp;
    LOOP
        FETCH c_emp INTO emp_id;
        EXIT WHEN c_empattr%notfound; 
        INSERT INTO EmployeeAttribute(employeeid, attributeid) VALUES (emp_id, attr_id);
    END LOOP;
    
    CLOSE c_emp;
    CLOSE c_attribute;
    COMMIT;
END;