package org.teste.memcached.tests;

import org.teste.memcached.utils.MemcachedPoolUtils;
import com.whalin.MemCached.MemCachedClient;

public class TesteNullValue {

	public static void main( String[] args ) {
		try{
			MemCachedClient mcc = MemcachedPoolUtils.initializePool(true);

			mcc.flushAll();
			
			String key = "KEY";

			boolean retSetNull = mcc.set(key, null);

			System.out.println("SET NULL: "+retSetNull);
			System.out.println("GET RETURN: "+mcc.get(key));
			
			boolean retSetNonNull = mcc.set(key, "valor");
			
			System.out.println("\nSET NON NULL: "+retSetNonNull);
			System.out.println("GET RETURN: "+mcc.get(key));

			boolean retSetNullWithNonNullKey = mcc.set(key, null);
			
			System.out.println("\nSET NULL AFTER SET: "+retSetNullWithNonNullKey);
			System.out.println("GET RETURN: "+mcc.get(key));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
