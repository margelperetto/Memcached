package org.teste.memcached;

import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.teste.memcached.utils.MemcachedPoolUtils;

import com.whalin.MemCached.MemCachedClient;

public class App {

	public static void main(String[] args) {
		if(args==null || args.length==0) {
			startFrame();
			return;
		}
		if(args.length < 2) {
			System.err.println("Host and port not informed!");
			return;
		}
		String address = args[0];
		Integer port = Integer.parseInt(args[1]);
		try {
			System.out.println("Connecting: "+address+":"+port);

			MemCachedClient mcc = MemcachedPoolUtils.initializePool(true, address, port);
			if(testOk(mcc)) {
				System.out.println("\nConnection success! "+address+":"+port);
			} else {
				System.err.println("\nConnection FAIL! "+address+":"+port);
				return;
			}
			
			if(args.length==2) {
				return;
			}
			
			String opt = args[2];
			if(opt.equalsIgnoreCase("STATS")) {
				showStats(mcc, address, port);
			} else if(opt.equalsIgnoreCase("CLEAR")) {
				clear(mcc, address, port);
			} else if(opt.equalsIgnoreCase("GET")) {
				get(mcc, args);
			}else if(opt.equalsIgnoreCase("SET")) {
				set(mcc, args);
			} else {
				System.err.println("\nInvalid option! '"+opt+"' \nUse STATS, CLEAR, GET or SET");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nConnection error! "+address+":"+port);
		}
	}
	
	private static void set(MemCachedClient mcc, String[] args) {
		if(args.length<5) {
			System.out.println("KEY/VALUE not informed!");
			return;
		}
		String key = args[3];
		StringBuilder sb = new StringBuilder();
		for (int i = 4; i < args.length; i++) {
			sb.append(args[i]).append(" ");
		}
		String value = sb.toString();
		value = value.substring(0, value.length()-1);
		boolean ret = mcc.set(key, value);
		System.out.println("\n\nKey: \n'"+key+"'");
		System.out.println("Value:   \n'"+value+"'");
		System.out.println("Success: \n'"+ret+"'");
	}

	private static void get(MemCachedClient mcc, String[] args) {
		if(args.length==3) {
			System.out.println("KEY not informed!");
			return;
		}
		String key = args[3];
		System.out.println("\n\nKey: \n'"+key+"'");
		System.out.println("Value:   \n'"+mcc.get(key)+"'");
	}

	private static void clear(MemCachedClient mcc, String address, Integer port) {
		mcc.flushAll(new String[]{address+":"+port});
		System.out.println("\nCache clear!");
	}

	private static void showStats(MemCachedClient mcc, String address, Integer port) {
		try {
			Map<String, Map<String, String>> stats = mcc.stats();
			if(stats==null || stats.isEmpty()){
				System.err.println("\nNo information available for: "+address+":"+port);
			}else{
				Map<String, String> serverStats = stats.get(address+":"+port);
				
				long memBytes = convertLong(serverStats.get("bytes"));
				long maxMemBytes = convertLong(serverStats.get("limit_maxbytes"));
				float p = (memBytes*100f)/maxMemBytes;
				DecimalFormat df = new DecimalFormat("###,##0.00");
				
				StringBuilder sb = new StringBuilder("\n");
				sb.append("MEMORY BYTES: \nUsed -> "+(memBytes/1024)+" kb / "+(maxMemBytes/1024/1024)+" mb ("+df.format(p)+"%)\n\n");
				sb.append("SERVER STATS: \n"+serverStats);

				System.out.println(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR STATS! \n"+e.getMessage());
		}
	}
	
	private static long convertLong(String str) {
		return str==null||str.trim().isEmpty()?0:Long.parseLong(str.trim());
	}

	private static boolean testOk(MemCachedClient mcc) {
		boolean retSet = mcc.set("key", "VALUE TEST");
		String retGet = (String) mcc.get("key");
		return retSet && retGet!=null && !retGet.isEmpty();
	}

	private static void startFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new ViewTest().setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
