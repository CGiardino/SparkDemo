package com.citi.spark.demo.streaming;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.esotericsoftware.minlog.Log;

public class SocketStream extends Thread {
	private static final Logger LOG = LogManager.getLogger(AbstractDriver.class);
	private String hostname;
	private int port;
	private ServerSocketChannel server;
	private volatile boolean isDone = false;
	private SocketChannel socket = null;
	private long totalBytes;
	private long totalLines;

	public SocketStream(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		totalBytes = 0;
		totalLines = 0;
	}

	public SocketChannel init() throws IOException {
		server = ServerSocketChannel.open();
		server.bind(new InetSocketAddress(hostname, port));
		LOG.info(String.format("Listening on %s", server.getLocalAddress()));
		return server.accept();
	}

	public void kickOff(SocketChannel socket) {
		Log.info("Kicking off data transfer");
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			while (!isDone) {
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public void sendMsg(String msg) throws IOException, InterruptedException, ExecutionException {
		if (socket != null) {
			ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
			int bytesWritten = socket.write(buffer);
			totalBytes += bytesWritten;
		} else {
			throw new IOException("Client hasn't connected yet!");
		}
		totalLines++;
	}

	public void done() {
		isDone = true;
	}

	public void close() throws IOException {
		if (socket != null) {
			socket.close();
			socket = null;
		}
		LOG.info(String.format("SocketStream is closing after writing %d bytes and %d lines", totalBytes, totalLines));
	}
}
