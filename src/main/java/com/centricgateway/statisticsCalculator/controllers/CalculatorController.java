package com.centricgateway.statisticsCalculator.controllers;

import com.centricgateway.statisticsCalculator.models.StatModel;
import com.centricgateway.statisticsCalculator.models.TranModel;
import com.centricgateway.statisticsCalculator.services.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class CalculatorController {

	@Autowired
	private TransactionDao transactionDao;

	/* Controller method for
	posting transactions */
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


	@GetMapping("/get")
	public ConcurrentHashMap get(){
		return transactionDao.getAllTransaction();
	}

	/* Returns statistics of transactions
	done withing the past 60 seconds */
	@GetMapping("/statistics")
	public StatModel statistic(){
		
		return transactionDao.getStatistics();
		
	}

	/* Controller method for deleting all
	* transactions*/
	@DeleteMapping("/transactions")
	public ResponseEntity<?> delete() {
		transactionDao.delTransactions();
		return new ResponseEntity(null, HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<?> handleException() {

		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ HttpMessageNotReadableException.class, DateTimeParseException.class })
	public ResponseEntity<?> handleExceptionHttpMessageNotReadable() {

		return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		
	}
}
