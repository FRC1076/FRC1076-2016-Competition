package org.usfirst.frc.team1076.test.mock;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import org.usfirst.frc.team1076.udp.IChannel;
import org.usfirst.frc.team1076.udp.UDPMessage;

public class MockChannel implements IChannel {
	Queue<UDPMessage> messageQueue = new LinkedList<UDPMessage>();
	
	public void addMessage(UDPMessage message) {
		messageQueue.add(message);
	}
	
	public void addMessage(String message) {
		addMessage(new UDPMessage(message, null));
	}
	
	@Override
	public boolean hasMessage() {
		return !messageQueue.isEmpty();
	}

	@Override
	public UDPMessage popLatestMessage() {
		return messageQueue.poll();
	}

	@Override
	public void sendMessage(String message, InetAddress target) throws IOException {
		// TODO Auto-generated method stub
		throw new IOException("Unimplemented");
	}

}
