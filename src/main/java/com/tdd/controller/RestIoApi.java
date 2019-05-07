package com.tdd.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestIoApi {
	
	
	@RequestMapping(value = "/io/testconnect" ,method = RequestMethod.GET)
	public String testConnection()
	{
		return "success";
	}
	
	
	@RequestMapping(value = "/io/copyfilebystream" ,method = RequestMethod.GET)
	public String copyFileByStream(@RequestParam String sourcePath, @RequestParam String destPath)
	{

		
		/*String sourcePath = "D:\\learning\\leaning-repository\\IO\\io.txt";
		String destPath = "D:\\learning\\leaning-repository\\IO\\io_stream_cp.txt";*/
		
		
		File destFile = new File(destPath);
		
		if(destFile.exists())
		{
			destFile.delete();
		}
		

		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		long wasteTime = System.currentTimeMillis();
		
		try {
			inputStream = new BufferedInputStream(new FileInputStream(new File(sourcePath)));
			outputStream = new BufferedOutputStream(new FileOutputStream(new File(destPath), true));
			
			byte[] byteAarry = new byte[512];
			
			while(inputStream.read(byteAarry)!=-1)
			{
				outputStream.write(byteAarry);
				//outputStream.flush();
				
			}
			
			outputStream.flush();
			
			System.out.println("copyfilebystream flush wasteTime:"+(System.currentTimeMillis() - wasteTime));
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "copyfilebystream finish wasteTime:"+(System.currentTimeMillis() - wasteTime);
		
	
	}
	
	@RequestMapping(value = "/io/copyfilebynio" ,method = RequestMethod.GET)
	public String copyFileByNio(@RequestParam String sourcePath, @RequestParam String destPath ,@RequestParam boolean delete)
	{
		File sourceFile = new File(sourcePath);
		File destFile = new File(destPath);

		FileChannel sourceCh = null;
		FileChannel destCh = null;
		
		if(destFile.exists() && delete)
		{
			destFile.delete();
		}

		long wasteTime = System.currentTimeMillis();

		try {

			sourceCh = new FileInputStream(sourceFile).getChannel();
			destCh = new FileOutputStream(destFile).getChannel();

			destCh.transferFrom(sourceCh, 0, sourceCh.size());

			System.out.println("copyfilebynio transfer wasteTime:" + (System.currentTimeMillis() - wasteTime));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				sourceCh.close();
				long closeTime = System.currentTimeMillis();
				destCh.close();
				System.out.println("copyfilebynio close wasteTime:" + (System.currentTimeMillis() - closeTime));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return "copyfilebynio fininsh wasteTime:" + (System.currentTimeMillis() - wasteTime);
	}
	
	
	@RequestMapping(value = "/io/readfilebynio" ,method = RequestMethod.GET)
	public String readFileByNio(@RequestParam String sourcePath)
	{
		FileInputStream file = null;

		FileChannel channel = null;
		
		long wasteTime = System.currentTimeMillis();

		try {
	
			// file = new RandomAccessFile(path, "rw");

			file = new FileInputStream(sourcePath);

			channel = file.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(512);

			buffer.clear();

			while (channel.read(buffer) != -1) {
				buffer.flip();

				while (buffer.hasRemaining()) {
					// System.out.println((char) buffer.get());
					buffer.get();
				}

				buffer.clear();
			}

			System.out.println("readfilebynio wasteTime:" + (System.currentTimeMillis() - wasteTime));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				file.close();
				channel.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return "readfilebynio wasteTime:" + (System.currentTimeMillis() - wasteTime);
	}
	
	
	@RequestMapping(value = "/io/readfilebystream" ,method = RequestMethod.GET)
	public String readFileByStream(@RequestParam String sourcePath)
	{

		InputStream inputStream = null;

		long wasteTime = System.currentTimeMillis();

		try {

			inputStream = new BufferedInputStream(new FileInputStream(new File(sourcePath)));

			byte[] byteAarry = new byte[512];

			while (inputStream.read(byteAarry) != -1) {

				String chart = new String(byteAarry, "utf8");
			}

			System.out.println("readfilebystream wasteTime:" + (System.currentTimeMillis() - wasteTime));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "readfilebystream wasteTime:" + (System.currentTimeMillis() - wasteTime);

	}

}
