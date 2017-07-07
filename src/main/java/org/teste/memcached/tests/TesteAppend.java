package org.teste.memcached.tests;

import java.util.Arrays;
import java.util.List;

import org.teste.memcached.utils.MemcachedPoolUtils;

import com.whalin.MemCached.MemCachedClient;

public class TesteAppend {

	public static void main( String[] args ) {
		try{

			MemCachedClient mcc = MemcachedPoolUtils.initializePool(true);

			mcc.flushAll();

			String ALL_KEYS = "ALL_KEYS";
			String SPLIT_CARACTERE = "&-&";

			for (int i = 0; i < 5; i++) {
				String valueKey = "key"+i+SPLIT_CARACTERE;
				if(!mcc.append(ALL_KEYS, valueKey)){
					mcc.set(ALL_KEYS, valueKey);
				}
			}

			String allKeysStr = (String) mcc.get(ALL_KEYS);
			List<String> listAllKeys = Arrays.asList(allKeysStr.split(SPLIT_CARACTERE));
			System.out.println("CHAVE: "+listAllKeys);
			
			System.out.println("Remover key: "+mcc.delete(ALL_KEYS));
			System.out.println("APÓS REMOVER: "+mcc.get(ALL_KEYS));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
