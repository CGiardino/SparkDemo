package com.citi.spark.demo.streaming;

import java.nio.channels.SocketChannel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SocketDriver extends AbstractDriver {
	private static final Logger LOG = LogManager.getLogger(AbstractDriver.class);
	private String hostname;
	private int port;
	private SocketStream socketStream;

	public SocketDriver(String path, String hostname, int port) {
		super(path);
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void init() throws Exception {
		socketStream = new SocketStream(hostname, port);
		LOG.info(String.format("Waiting for client to connect on port %d", port));
		SocketChannel socketChan = socketStream.init();
		LOG.info(String.format("Client %s connected on port %d", socketChan.getRemoteAddress(), port));
		socketStream.kickOff(socketChan);
		socketStream.start();
	}

	@Override
	public void close() throws Exception {
		socketStream.done();
		if (socketStream != null) {
			socketStream.close();
		}
	}

	@Override
	public void sendRecord(String record) throws Exception {
		socketStream.sendMsg(record + '\n');
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			LOG.error("Usage: SocketDriver <path_to_input_folder> <hostname> <port>");
			System.exit(-1);
		}
		String path = args[0];
		String hostname = args[1];
		int port = Integer.parseInt(args[2]);
		SocketDriver driver = new SocketDriver(path, hostname, port);
		try {
			driver.execute();
		} finally {
			driver.close();
		}
	}

}
