package com.microservico.msuser.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservico.msuser.model.PageWrapper;
import com.microservico.msuser.model.User;
import com.microservico.msuser.repository.UserRepository;
import com.microservico.msuser.util.DateTimeUtil;

@RestController
@RequestMapping(value = "/ms/users")
public class UserController {

	@Autowired
	private UserRepository repository;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageWrapper<User> getList(@RequestParam(value = "page", defaultValue = "0") int page, 
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "descricao") String sort) {
        Pageable pageableRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<User> itens = repository.findAll(pageableRequest);
        PageWrapper<User> pageWrapper = new PageWrapper<User>(itens, "/list");
		return pageWrapper;
	}
	
	@RequestMapping(value = "/listSimples", method = RequestMethod.GET)
	public List<User> getList() {
		return (List<User>) repository.findAll();
	}	

	@RequestMapping(method = RequestMethod.GET)
	public Optional<User> getById(@RequestParam(value="id") String id) {
		return repository.findById(new ObjectId(id));
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public List<User> findByName(@RequestParam(value="descricao") String descricao) {
		return repository.findByName(descricao);
	}
	
	@RequestMapping(value = "/findUsername", method = RequestMethod.GET)
	public List<User> findByUsername(@RequestParam(value="tipo") String tipo) {
		return repository.findByUsername(tipo);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void save(@Valid @RequestBody User obj) {
		if (StringUtils.isEmpty(obj.getDataCriacao())) {
			obj.setId(ObjectId.get());
			obj.setDataCriacao(DateTimeUtil.dataHoraAtual());
		} else {
			obj.setDataAlteracao(DateTimeUtil.dataHoraAtual());
		}
		if (!StringUtils.isEmpty(obj.getMotivoInativacao()) && StringUtils.isEmpty(obj.getDataInativacao())) {
			obj.setDataInativacao(DateTimeUtil.dataHoraAtual());
		} else if (StringUtils.isEmpty(obj.getMotivoInativacao()) && obj.getDataInativacao() != null) {
			obj.setDataInativacao(null);
		}
		repository.save(obj);
	}

}
