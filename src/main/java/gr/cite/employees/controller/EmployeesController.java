package gr.cite.employees.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.cite.employees.model.Employee;
import gr.cite.employees.repository.EmployeesRepository;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
	@Autowired EmployeesRepository employeesRepository;
	
	@GetMapping
	public List<Employee> getEmployees(){
		return employeesRepository.findAll();
	}
	
	@PutMapping
	public Employee addEmployee(@RequestBody Employee employee) throws Exception {
		if(employee.getName()==null || employee.getName().isEmpty())
			throw new Exception("Name cannot be empty");
		if(employee.getHireDate()==null)
			throw new Exception("Hire date cannot be empty");
		
		if(employee.getSupervisorName()!=null && !employee.getSupervisorName().isEmpty()) {
			Long supervisorId = employeesRepository.findSupervisor(employee.getSupervisorName());
			employee.setSupervisorId(supervisorId);
		}
		
		return employeesRepository.create(employee);
	}
	
	@PostMapping
	public ResponseEntity updateEmployee(@RequestBody Employee employee) throws Exception {
		if(employee.getName()==null || employee.getName().isEmpty())
			throw new Exception("Name cannot be empty");
		if(employee.getHireDate()==null)
			throw new Exception("Hire date cannot be empty");
		if(employee.getId()==null)
			throw new Exception("You have to provide the employee id");
		
		if(employee.getSupervisorName()!=null && !employee.getSupervisorName().isEmpty()) {
			Long supervisorId = employeesRepository.findSupervisor(employee.getSupervisorName());
			employee.setSupervisorId(supervisorId);
		}
			
		employeesRepository.update(employee);
		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity removeEmployee(@PathVariable("id") Long employeeId) {
		employeesRepository.delete(employeeId);
		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).build();
	}
}
