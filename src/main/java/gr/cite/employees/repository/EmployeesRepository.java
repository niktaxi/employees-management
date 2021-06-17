package gr.cite.employees.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gr.cite.employees.model.Employee;

@Repository
public class EmployeesRepository {
	@Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Employee> findAll() {
        return jdbcTemplate.query("select E1.*, E2.name as supervisor from employee E1 left join employee E2 on E1.supervisor_id=E2.id order by E1.id", 
                new EmployeeRowMapper());
    }
    
    public Long findSupervisor(String name) {
        return (Long) jdbcTemplate.queryForObject("select id from employee where name=?", new Object[] {name}, 
                Long.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public Employee create(final Employee employee) 
    {
    	Long nextId = jdbcTemplate.queryForObject("select employee_seq.NEXTVAL from dual", Long.class);
    	
        final String sql = "insert into employee(id, name, hire_day, supervisor_id) values(?, ?, ?, ?)";

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, nextId);
                ps.setString(2, employee.getName());
                ps.setDate(3, new java.sql.Date(employee.getHireDate().getTime()));
                if(employee.getSupervisorId()!=null)
                	ps.setLong(4, employee.getSupervisorId());
                else
                	ps.setNull(4, Types.INTEGER);
                return ps;
            }
        });
        employee.setId(nextId);
        return employee;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(final Employee employee) 
    {
        final String sql = "update employee set name=?, hire_day=?, supervisor_id=? where id=?";
        
        jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				 PreparedStatement ps = connection.prepareStatement(sql);
	                ps.setString(1, employee.getName());
	                ps.setDate(2, new java.sql.Date(employee.getHireDate().getTime()));
	                ps.setLong(3, employee.getSupervisorId());
	                ps.setLong(4, employee.getId());
	                return ps;
			}
        	
        });
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long employeeId) {
    	List<String> attrIds = jdbcTemplate.queryForList("select attributeid from EmployeeAttribute where EmployeeID=?", String.class, new Object[]{employeeId});
    	jdbcTemplate.execute("delete from EmployeeAttribute where EmployeeID="+employeeId);
    	jdbcTemplate.execute("delete from employee where id="+employeeId);
    	if(attrIds!= null && !attrIds.isEmpty()) { //if the employee has got attributes, then we should delete them, as well
    		String attrIdsStr = StringUtils.join(attrIds, ',');
    		jdbcTemplate.execute("delete from attribute where id IN (" + attrIdsStr +")");
    	}
    }
    
    private class EmployeeRowMapper implements RowMapper<Employee>
    {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Employee employee = new Employee();
        	employee.setId(rs.getLong("id"));
        	employee.setName(rs.getString("name"));
        	employee.setHireDate(rs.getDate("hire_day"));
        	employee.setSupervisorId(rs.getLong("supervisor_id"));
        	employee.setSupervisorName(rs.getString("supervisor"));
            return employee;
        }
    }
}
