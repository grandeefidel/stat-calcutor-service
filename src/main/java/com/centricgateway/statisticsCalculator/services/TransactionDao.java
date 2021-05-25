package com.centricgateway.statisticsCalculator.services;

import com.centricgateway.statisticsCalculator.models.StatModel;
import com.centricgateway.statisticsCalculator.models.TranModel;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransactionDao {

	//Map for holding data
	private ConcurrentHashMap<String, TranModel> tranMap;

	private final String created = "201";
	private final String noContent = "204";

	//	constructor
	public TransactionDao() {
		//Instantiate ConcurrentMap that will hold the transactions
		this.tranMap = new ConcurrentHashMap<String, TranModel>();
	}

	/* Service for
	saving transactions */
	public String storeTransaction(TranModel tranModel) {
		Random rand = new Random();
		int random = rand.nextInt(1012);
		try{
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime getTimeStamp = tranModel.getTimestamp();
			if(getTimeStamp.isBefore(currentTime.minusSeconds(60))) {
				return noContent;
			}else if(getTimeStamp.isAfter(currentTime)) {
				//	private final String badRequest = "400";
				String unprocessable = "422";
				return unprocessable;
			}
			tranMap.put(tranModel.getTimestamp().toString() + random, tranModel);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return created;
	}

	/* Service for
	saving transactions */
	public void delTransactions() {
		tranMap.clear();
	}

	public ConcurrentHashMap getAllTransaction() {
		// TODO Auto-generated method stub
		return tranMap;
	}

	/* Returns statistics of transactions
	done withing the past 60 seconds to the controller*/
	public StatModel getStatistics() {
		StatModel statModel = new StatModel();
		List<BigDecimal> listOfAmount = new ArrayList<>();
		LocalDateTime currentTime = LocalDateTime.now();
		for(Map.Entry<String, TranModel> entry: tranMap.entrySet()) {
			if(entry.getValue().getTimestamp().isAfter(currentTime.minusSeconds(60))) {
				listOfAmount.add(entry.getValue().getAmount());
			}
		}
		if(listOfAmount.size() > 0) {
			BigDecimal sum = BigDecimal.ZERO;
				for (BigDecimal amt : listOfAmount) {
		            sum = sum.add(amt);
		        }
			statModel.setSum(sum);
			statModel.setAvg(sum.divide(new BigDecimal(listOfAmount.size())));
			statModel.setMax(listOfAmount.stream().max(BigDecimal::compareTo).get());
			statModel.setMin(listOfAmount.stream().min(BigDecimal::compareTo).get());
			statModel.setCount(Long.valueOf(listOfAmount.size()));
			
		}else {
			statModel.setAvg(BigDecimal.ZERO);
			statModel.setSum(BigDecimal.ZERO);
			statModel.setMax(BigDecimal.ZERO);
			statModel.setMin(BigDecimal.ZERO);
			statModel.setCount(0L);
		}

		return statModel;
	}
}
