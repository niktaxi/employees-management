DECLARE 
    attr_id attribute.id%type; 
    emp_id employee.id%type;
    CURSOR c_emp IS /*query all supervisors*/
        SELECT DISTINCT E.id 
        FROM Employee E
        INNER JOIN Employee E2
        ON E.id=E2.supervisor_id;
    CURSOR c_empattr IS /*query all supervisors that have a height attribute*/
        WITH bosses AS (
            SELECT DISTINCT E.id 
            FROM Employee E
            INNER JOIN Employee E2
            ON E.id=E2.supervisor_id
        )
        SELECT B.id, EA.attributeid
        FROM EmployeeAttribute EA
        INNER JOIN bosses B
        ON EA.EmployeeId=B.id
        INNER JOIN Attribute A
        ON A.id=EA.attributeId
        WHERE A.name='Height';
    
BEGIN

    OPEN c_empattr;
    
    IF c_empattr%rowcount = 0 THEN
        OPEN c_emp;
        
        SELECT id INTO attr_id FROM Attribute WHERE name='Height' AND value='Short' FETCH FIRST 1 ROWS ONLY;
        IF attr_id IS NULL THEN
            SELECT attr_seq.nextval INTO attr_id FROM dual;
            INSERT INTO Attribute(id, name, value) VALUES(attr_id, 'Height', 'Short');
        END IF;
        
        LOOP
            FETCH c_emp INTO emp_id;
            EXIT WHEN c_emp%notfound; 
            INSERT INTO EmployeeAttribute(employeeId, attributeId) VALUES(emp_id, attr_id);
        END LOOP;
        dbms_output.put_line( c_emp%rowcount || ' rows inserted into EmployeeAttribute'); 
        CLOSE c_emp;
    ELSE
        LOOP
            FETCH c_empattr INTO emp_id, attr_id;
            UPDATE Attribute SET value='Short' WHERE id=attr_id;
            EXIT WHEN c_empattr%notfound; 
        END LOOP;
        dbms_output.put_line( c_empattr%rowcount || ' rows updated in Attribute');
    END IF;
    CLOSE c_empattr;
    COMMIT;
END;