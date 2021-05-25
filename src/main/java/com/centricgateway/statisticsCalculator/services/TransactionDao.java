package com.centricgateway.statisticsCalculator.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.centricgateway.statisticsCalculator.models.TranModel;
import com.centricgateway.statisticsCalculator.models.StatModel;

@Service
public class TransactionDao {
	
	private ConcurrentHashMap<String, TranModel> tranMap;
	private final String created = "201";
	private final String noContent = "204";
	private final String badRequest = "400";
	private final String unprocessable  = "422";

	@Autowired
	private HttpServletRequest httpServletReq;
	
	@Autowired
	private Environment env;

	public TransactionDao() {
		this.tranMap = new ConcurrentHashMap<String, TranModel>();
	}
	
	public String storeTransaction(TranModel tranModel) {
		
		try{
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime getTimeStamp = tranModel.getTimestamp();
			if(getTimeStamp.isBefore(currentTime.minusSeconds(60))) {
				return noContent;
			}else if(getTimeStamp.isAfter(currentTime)) {
				return unprocessable;
			}
			
			tranMap.put(tranModel.getTimestamp().toString(), tranModel);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return created;
	}
	
	public void delTransactions() {
		tranMap.clear();
	}

	public ConcurrentHashMap getAllTransaction() {
		// TODO Auto-generated method stub
		return tranMap;
	}

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
