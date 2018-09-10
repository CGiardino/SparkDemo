package com.citi.spark.demo.streaming;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;

public class DemoStreaming {

	private static final Logger LOG = LogManager.getLogger(AbstractDriver.class);

	public static void main(String args[]) {

		if (args.length < 2) {
			LOG.error("Usage: tribByYearApp <hostname> <port>");
			System.exit(-1);
		}

		SparkConf conf = new SparkConf().setAppName("Demo Streaming").setMaster("local[2]");
		StreamingContext sc = new StreamingContext(conf, Durations.seconds(10));
	}
}
