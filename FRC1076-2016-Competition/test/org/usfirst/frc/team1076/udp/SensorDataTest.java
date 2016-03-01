package org.usfirst.frc.team1076.udp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team1076.test.mock.MockChannel;
import org.usfirst.frc.team1076.udp.SensorData.FieldPosition;

public class SensorDataTest {
	private static final double EPSILON = 1e-12;
	MockChannel channel;
	SensorData data;
	
	@Before
	public void setupChannelAndData() {
		channel = new MockChannel();
		data = new SensorData(channel, FieldPosition.Left);
	}
	
	@Test
	public void testSonarMessage() {
		String message = "{\"sender\": \"sonar\","
				+ "\"message\": \"ranges\","
				+ "\"left side back\": 1,"
				+ "\"right side back\": 2,"
				+ "\"left side front\": 3,"
				+ "\"right side front\": 4,"
				+ "\"left front\": 5,"
				+ "\"right front\": 6}";
		channel.addMessage(message);
		data.interpretData();
		assertEquals(1, data.getLeftSideBack(), EPSILON);
		assertEquals(2, data.getRightSideBack(), EPSILON);
		assertEquals(3, data.getLeftSideFront(), EPSILON);
		assertEquals(4, data.getRightSideFront(), EPSILON);
		assertEquals(5, data.getLeftFront(), EPSILON);
		assertEquals(6, data.getRightFront(), EPSILON);
	}
	
	@Test
	public void testVisionMessageOk() {
		String message = "{\"sender\": \"vision\","
				+ "\"message\": \"heading and range\","
				+ "\"status\": \"ok\","
				+ "\"heading\": 42,"
				+ "\"range\": 12}";
		channel.addMessage(message);
		data.interpretData();
		
		assertEquals(42, data.getHeading(), EPSILON);
		assertEquals(12, data.getDistance(), EPSILON);
	}	
	
	@Test
	public void testVisionMessageSides() {
		data.setFieldPosition(FieldPosition.Left);
		String leftMessage = "{\"sender\": \"vision\","
				+ "\"message\": \"heading and range\","
				+ "\"status\": \"left\","
				+ "\"heading\": 1,"
				+ "\"range\": 2}";
		String rightMessage = "{\"sender\": \"vision\","
				+ "\"message\": \"heading and range\","
				+ "\"status\": \"right\","
				+ "\"heading\": 3,"
				+ "\"range\": 4}";
		channel.addMessage(leftMessage);
		channel.addMessage(rightMessage);
		data.interpretData();
		
		assertEquals(1, data.getHeading(), EPSILON);
		assertEquals(2, data.getDistance(), EPSILON);
	
		data.setFieldPosition(FieldPosition.Right);
		channel.addMessage(leftMessage);
		channel.addMessage(rightMessage);
		data.interpretData();
		
		assertEquals(3, data.getHeading(), EPSILON);
		assertEquals(4, data.getDistance(), EPSILON);
	}
}
