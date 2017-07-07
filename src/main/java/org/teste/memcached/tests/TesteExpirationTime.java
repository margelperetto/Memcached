package org.teste.memcached.tests;

import java.util.Date;

import org.teste.memcached.utils.MemcachedPoolUtils;

import com.whalin.MemCached.MemCachedClient;

public class TesteExpirationTime {

	public static void main( String[] args ) {
		try{
			MemCachedClient mcc = MemcachedPoolUtils.initializePool(true);

			mcc.flushAll();

			String key = "URL";
			String value = "http://teste.com";

			Date date = new Date(System.currentTimeMillis()+2000);
			mcc.set(key, value, date);

			System.out.println("Antes: "+mcc.get(key));
			Thread.sleep(2050);
			System.out.println("Depois: "+mcc.get(key));


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
