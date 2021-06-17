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

import gr.cite.employees.model.Attribute;
import gr.cite.employees.repository.AttributesRepository;

@RestController
@RequestMapping("/attributes")
public class AttributesController {
	@Autowired AttributesRepository attributesRepository;
	
	@GetMapping("/{employeeId}")
	public List<Attribute> getAttributes(@PathVariable("employeeId") Long employeeId){
		return attributesRepository.findAll(employeeId);
	}
	
	@PutMapping("/{employeeId}")
	public Attribute addAttribute(@PathVariable("employeeId") Long employeeId, @RequestBody Attribute attr) throws Exception {
		if(attr.getName()==null || attr.getName().isEmpty())
			throw new Exception("Name cannot be empty");
		if(attr.getValue()==null || attr.getValue().isEmpty())
			throw new Exception("Value cannot be empty");
		
		return attributesRepository.create(attr, employeeId);
	}
	
	@PostMapping
	public ResponseEntity updateAttribute(@RequestBody Attribute attr) throws Exception {
		if(attr.getName()==null || attr.getName().isEmpty())
			throw new Exception("Name cannot be empty");
		if(attr.getValue()==null || attr.getValue().isEmpty())
			throw new Exception("Value cannot be empty");
		if(attr.getId()==null)
			throw new Exception("You have to provide the attribute id");
		
		attributesRepository.update(attr);
		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).build();
	}
	
	@DeleteMapping("/{attrId}")
	public ResponseEntity deleteAttribute(@PathVariable("attrId") Long attrId) {
		attributesRepository.delete(attrId);
		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).build();
	}
}
