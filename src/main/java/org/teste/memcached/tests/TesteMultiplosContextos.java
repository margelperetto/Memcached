package org.teste.memcached.tests;

import java.io.Serializable;

import org.teste.memcached.utils.MemcachedPoolUtils;
import org.teste.memcached.utils.SerializeUtils;

import com.whalin.MemCached.MemCachedClient;

public class TesteMultiplosContextos {
	
	public static void main( String[] args ) {
		
		MemCachedClient mcc = MemcachedPoolUtils.initializePool(true);
		
		try{
				
			String keyURL = "URL";
			String keyBD = "BD";
			
			System.out.println("");
			
			for (int context = 1; context <= 5; context++) {
				String valueURL = "http://teste"+context+".org";
				String valueBD = "sesuite_"+context;
				setInContext(mcc, context, keyURL, valueURL);
				setInContext(mcc, context, keyBD, valueBD);
				
				if(context==3){
					setInContext(mcc, context, "KEY_TESTE3", "VALUE_TESTE3");
				}
			}
			
			for (int context = 1; context <= 5; context++) {
				String retURL = getFromContext(mcc, context, keyURL);
				String retBD = getFromContext(mcc, context, keyBD);
				
				System.out.println("Retornos para o contexto: "+context);
				System.out.println("    URL : "+retURL);
				System.out.println("    BD  : "+retBD);
				
				if(context==3){
					String ret3 = getFromContext(mcc, context, "KEY_TESTE3");
					System.out.println("    KEY3 : "+ret3);
				}
				
				System.out.println("------------------------------\n");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void setInContext(MemCachedClient jedis, int contextIndex, String key, Serializable value) throws Exception{
		jedis.set(contextIndex+key, SerializeUtils.toJSON(value));
	}
	
	private static <T> T getFromContext(MemCachedClient jedis, int contextIndex, String key)  throws Exception{
		return SerializeUtils.fromJSON((String)jedis.get(contextIndex+key));
	}
	

}
