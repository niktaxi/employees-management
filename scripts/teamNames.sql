:supervisor_id;
DECLARE
    attr_id attribute.id%type; 
    v_name employee.name%type;
    emp_id employee.id%type;
    CURSOR c_emp IS /*query all supervised employees*/
        with  CTE(id, levell) as 
        (
            select  id, 1 AS levell
            from    Employee
            where   id = :supervisor_id
            
            union all
            
            select  child.id, levell + 1 AS levell
            from    Employee child
            inner join    CTE parent
            on      child.supervisor_id = parent.id
        )
        select  id
        from    CTE
        WHERE levell>1;
    
BEGIN
    SELECT name INTO v_name FROM Employee WHERE id=:supervisor_id;
    SELECT attr_seq.nextval INTO attr_id FROM dual;
    INSERT INTO Attribute(id, name, value) VALUES(attr_id, 'Team', v_name);
    dbms_output.put_line( 'Attribute record inserted');
    
    OPEN c_emp;
    LOOP
        FETCH c_emp INTO emp_id;
        EXIT WHEN c_emp%notfound;
        INSERT INTO EmployeeAttribute(employeeId, attributeId) VALUES(emp_id, attr_id);
    END LOOP;
    dbms_output.put_line( c_emp%rowcount || ' rows inserted in EmployeeAttribute');
    CLOSE c_emp;
    COMMIT;
END;