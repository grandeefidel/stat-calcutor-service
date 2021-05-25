/**
 * 
 */
package com.centricgateway.statisticsCalculator.controllers;

import com.centricgateway.statisticsCalculator.StatisticsCalculatorApplication;
import com.centricgateway.statisticsCalculator.models.StatModel;
import com.centricgateway.statisticsCalculator.models.TranModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author fidelis.chukwurah
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StatisticsCalculatorApplication.class)
@WebAppConfiguration
public class CalculatorControllerTest {
	@Autowired
	private ObjectMapper objectMapper;

	protected MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}


	@Test
	public void storeWillReturn201WhenTransactionIsWithin60Seconds() throws Exception {
		String uri = "/transactions";
		TranModel tranModel = new TranModel();
		tranModel.setAmount(new BigDecimal(14.33).round(new MathContext(4, RoundingMode.HALF_EVEN)));
		LocalDateTime dateTime = LocalDateTime.now();
	      System.out.println("Current date-time: "+dateTime);
//		tranModel.setTimestamp(LocalDateTime.parse("2021-05-25T14:14:16.267"));
		tranModel.setTimestamp(dateTime.minusSeconds(30));
//	    String inputJson = super.mapToJson(tranModel);
		String inputJson = objectMapper.writeValueAsString(tranModel);
	    System.out.println(inputJson);
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
	         .contentType(MediaType.APPLICATION_JSON_VALUE)
	         .content(inputJson)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(201, status);
	}

	@Test
	public void storeWillReturn204WhenTransactionIsOlderThan60Seconds() throws Exception {
		String uri = "/transactions";
		TranModel tranModel = new TranModel();
		tranModel.setAmount(new BigDecimal(14.33).round(new MathContext(4, RoundingMode.HALF_EVEN)));
		LocalDateTime dateTime = LocalDateTime.now();
		System.out.println("Current date-time: "+dateTime);
		tranModel.setTimestamp(LocalDateTime.parse("2021-05-25T14:14:16.267"));
//		tranModel.setTimestamp(dateTime.minusSeconds(30));
		String inputJson = objectMapper.writeValueAsString(tranModel);
		System.out.println(inputJson);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}

	@Test
	public void storeWillReturn400WhenRequestIsIvalid() throws Exception {
		String uri = "/transactions";
		TranModel tranModel = new TranModel();
		tranModel.setAmount(new BigDecimal(14.33).round(new MathContext(4, RoundingMode.HALF_EVEN)));
		LocalDateTime dateTime = LocalDateTime.now();
		System.out.println("Current date-time: "+dateTime);
//		tranModel.setTimestamp(LocalDateTime.parse("2021-05-25T14:14:16.267"));
		String inputJson = objectMapper.writeValueAsString(tranModel);
		System.out.println(inputJson);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	
	 @Test
	   public void getstatistics() throws Exception {
	      String uri = "/statistics";
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	         .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(200, status);
	      String content = mvcResult.getResponse().getContentAsString();
	      StatModel statModel = objectMapper.readValue(content, StatModel.class);
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
