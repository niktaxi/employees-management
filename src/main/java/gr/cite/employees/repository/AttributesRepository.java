package gr.cite.employees.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gr.cite.employees.model.Attribute;

@Repository
public class AttributesRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Attribute> findAll(Long employeeId) {
        return jdbcTemplate.query("select A.* from Attribute A inner join EmployeeAttribute EA on EA.AttributeID=A.id where EA.EmployeeID=? order by AttributeID", new Object[] {employeeId},
                new AttributeRowMapper());
    }

    @Transactional(rollbackFor = Exception.class)
    public Attribute create(final Attribute attr, Long employeeId) 
    {
    	Long nextId = jdbcTemplate.queryForObject("select attr_seq.NEXTVAL from dual", Long.class);
        final String sql = "insert into Attribute(id, name, value) values(?, ?, ?)";

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, nextId);
                ps.setString(2, attr.getName());
                ps.setString(3, attr.getValue());
                return ps;
            }
        });
        
        final String sql2 = "insert into EmployeeAttribute(EmployeeID, AttributeID) values(?, ?)";
        
        jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				 PreparedStatement ps = connection.prepareStatement(sql2);
				 ps.setLong(1, employeeId);
				 ps.setLong(2, nextId);
	             return ps;
			}
        });
        
        attr.setId(nextId);
        return attr;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(final Attribute attr) 
    {
        final String sql = "update Attribute set name=?, value=? where id=?";
        
        jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				 PreparedStatement ps = connection.prepareStatement(sql);
	                ps.setString(1, attr.getName());
	                ps.setString(2, attr.getValue());
	                ps.setLong(3, attr.getId());
	                return ps;
			}
        	
        });
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long attrId) {
    	jdbcTemplate.execute("delete from EmployeeAttribute where AttributeID="+attrId);
    	jdbcTemplate.execute("delete from Attribute where id="+attrId);
    }
    
    private class AttributeRowMapper implements RowMapper<Attribute>
    {
        @Override
        public Attribute mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Attribute attr = new Attribute();
        	attr.setId(rs.getLong("id"));
        	attr.setName(rs.getString("name"));
        	attr.setValue(rs.getString("value"));
            return attr;
        }
    }
}
