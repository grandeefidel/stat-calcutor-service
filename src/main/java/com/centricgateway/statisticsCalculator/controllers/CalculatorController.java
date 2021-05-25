package com.centricgateway.statisticsCalculator.controllers;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.centricgateway.statisticsCalculator.models.TranModel;
import com.centricgateway.statisticsCalculator.models.StatModel;
import com.centricgateway.statisticsCalculator.services.TransactionDao;


@RestController
public class CalculatorController {

	@Autowired
	private TransactionDao transactionDao;

	@PostMapping("/transactions")
	public ResponseEntity<?> store(@Valid @RequestBody TranModel tranModel) {

		String res = transactionDao.storeTransaction(tranModel);
		if (res.equalsIgnoreCase("201"))
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		else if (res.equalsIgnoreCase("204"))
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		else if (res.equalsIgnoreCase("422"))
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

//	@GetMapping("/statistics")
	@ResponseStatus(HttpStatus.OK)
//	public  get(@RequestBody Bike bike){
////		bikeRepository.save(bike);
//	}
	
	@GetMapping("/get")
	public ConcurrentHashMap get(){
		return transactionDao.getAllTransaction();
	}
	
	@GetMapping("/statistics")
	public StatModel statistic(){
		
		return transactionDao.getStatistics();
		
	}

	@DeleteMapping("/transactions")
	public ResponseEntity<?> delete() {
		transactionDao.delTransactions();
		return new ResponseEntity(null, HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<?> handleException() {

		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<?> handleExceptionHttpMessageNotReadable() {

		return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		
	}
}
