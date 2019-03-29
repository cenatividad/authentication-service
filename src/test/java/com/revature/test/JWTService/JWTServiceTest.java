package com.revature.test.JWTService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.JWTServices.JWTService;

/**
 * Only had time to add one test. It can be used as an example for testing other
 * methods in this class.
 * 
 * @author Kyne Liu
 *
 */
@RunWith(SpringRunner.class)
public class JWTServiceTest {
	
	@InjectMocks
	private JWTService testService;
	
	@Before
	public void init() {
		/*
		 * For mocking methods in the test class use Mockito spy to
		 * setup the test class. Then use the doReturn, doThrow, etc.
		 * methods to mock the method and call the method you are testing
		 * with the spy as well.
		 */
		testService = Mockito.spy(JWTService.class);
	}
	
	@Test
	public void successfulGenerateToken() {
		String email = "user@gmail.com";
		String name = "Name";
		String returned = "encryptedToken";

		//mocking the method used withing the method being tested
		Mockito.doReturn(returned).when(testService).Encrypter(any(), any());
		
		String result = null;
		try {
			//actually calling the method being tested
			result = testService.generateToken(email, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertNotNull("Method should not return null value on success.", result);
		assertThat("Method should return the encrypted token.", result, is(returned));
	}
}
