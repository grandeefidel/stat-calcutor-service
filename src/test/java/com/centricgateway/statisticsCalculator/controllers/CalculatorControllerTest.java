/**
 * 
 */
package com.centricgateway.statisticsCalculator.controllers;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.centricgateway.statisticsCalculator.models.StatModel;
import com.centricgateway.statisticsCalculator.models.TranModel;

/**
 * @author fidelis.chukwurah
 *
 */
public class CalculatorControllerTest extends AbstractTest {
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void store() throws Exception {
		String uri = "/transactions";
		TranModel tranModel = new TranModel();
		tranModel.setAmount(new BigDecimal(14.33));
		LocalDateTime dateTime = LocalDateTime.now();
	      System.out.println("Current date-time: "+dateTime);
		tranModel.setTimestamp(dateTime);
	    String inputJson = super.mapToJson(tranModel);
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
	         .contentType(MediaType.APPLICATION_JSON_VALUE)
	         .content(inputJson)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(201, status);
	}
	
	 @Test
	   public void getstatistics() throws Exception {
	      String uri = "/statistics";
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	         .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(200, status);
	      String content = mvcResult.getResponse().getContentAsString();
	      StatModel statModel = super.mapFromJson(content, StatModel.class);
	      assertTrue(statModel != null);
	   }
	
	 @Test
	   public void delete() throws Exception {
	      String uri = "/transactions";
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(204, status);
	   }

}
