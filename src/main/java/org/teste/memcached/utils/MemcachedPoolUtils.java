package org.teste.memcached.utils;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class MemcachedPoolUtils {
	
	private static final String POOL_ID = "POOL_ID";

	private static SockIOPool pool;
	
	public static MemCachedClient newClientFromPool() {
		return new MemCachedClient(MemcachedPoolUtils.POOL_ID);
	}

	public synchronized static MemCachedClient initializePool(boolean startClient, String server, Integer port) {
		
		shutDownPool();
		
		String ip = server;
		String portStr = String.valueOf(port);
		
		String[] servers = {ip+":"+portStr};
		pool = SockIOPool.getInstance(POOL_ID);
		
		pool.setServers( servers );
		pool.setFailover( true );
		pool.setNagle( false );
		pool.setAliveCheck( true );
		
		pool.setInitConn( 10 );
		pool.setMinConn( 10 );
		pool.setMaxConn( 250 );
		
		pool.setMaxIdle( 30_000 ); //TIMEOUT de ESPERA por uma conexão;
		pool.setSocketTO( 60_000 ); //TIMEOUT de uma inserção/leitura ao cache;
		pool.setMaintSleep( 1_000 ); //Tempo que uma conexão é mantida no pool;
		
		pool.initialize();
		
		return startClient?newClientFromPool():null;
	}

	public static void shutDownPool() {
		try {
			if(pool!=null){
				pool.shutDown();
				pool = null;
			}
		} catch (Exception e) {
			System.err.println("ERROR SHUTDOWN POOL!");
		}
	}
	
}
