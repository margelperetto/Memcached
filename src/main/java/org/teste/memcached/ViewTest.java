package org.teste.memcached;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.teste.memcached.utils.MemcachedPoolUtils;

import com.whalin.MemCached.MemCachedClient;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ViewTest extends JFrame{

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private JPanel jpTestsOperations = new JPanel();
	private JTextField jtfIpServer = new JTextField("192.168.1.186");
	private JSpinner jsPort = new JSpinner(new SpinnerNumberModel(11211, 0, 999999, 1));
	private JButton jbTestConnection = new JButton("Test connection");
	private JButton jbStats = new JButton("Stats");
	private JButton jbFlushAll = new JButton("FlushAll");
	private JTextArea jtaConsole = new JTextArea();

	private JComboBox<String> jcbOption = new JComboBox<>(new String[]{"GET","SET"});
	private JTextField jtfKey = new JTextField("");
	private JTextArea jtaValue = new JTextArea();
	private JButton jbTestGetSet = new JButton("OK");
	private JLabel jlLastExec = new JLabel(" ");
	
	private String address;
	private int port;

	public ViewTest() {
		
		jsPort.setEditor(new JSpinner.NumberEditor(jsPort, "00000"));
		
		JPanel jpConnection = new JPanel(new MigLayout(new LC().insetsAll("0")));
		jpConnection.add(new JLabel("Memcached server address"));
		jpConnection.add(new JLabel("Connection port"), new CC().wrap());
		jpConnection.add(jtfIpServer, new CC().width("200::"));
		jpConnection.add(jsPort, new CC().growX());
		jpConnection.add(jbTestConnection, new CC());
		
		JScrollPane scrollValue = new JScrollPane(jtaValue);
		scrollValue.setBorder(new TitledBorder("Value"));
		
		jcbOption.addActionListener((evt)->{
			scrollValue.setVisible(jcbOption.getSelectedIndex()==1);
			jtfKey.requestFocus();
			contentPaneUpdateUI();
		});
		
		jpTestsOperations.setLayout(new MigLayout(new LC().insetsAll("0").hideMode(2)));
		jpTestsOperations.add(jcbOption, new CC());
		jpTestsOperations.add(new JLabel("KEY"));
		jpTestsOperations.add(jtfKey, new CC().width("100:100%:"));
		jpTestsOperations.add(jbTestGetSet, new CC().gapRight("20"));
		jpTestsOperations.add(jbFlushAll, new CC());
		jpTestsOperations.add(jbStats, new CC().wrap());
		jpTestsOperations.add(scrollValue, new CC().spanX().width("0:100%:").height("80:100%:"));
		
		jbTestConnection.addActionListener((evt)->{
			testConnection();
		});
		jbTestGetSet.addActionListener((evt)->{
			testGetSet();
		});
		jbStats.addActionListener((evt)->{
			showStats();
		});
		jbFlushAll.addActionListener((evt)->{
			flushAll();
		});
		jtfKey.addActionListener((evt)->jbTestGetSet.doClick());
		
		jtaConsole.setEditable(false);

		setLayout(new MigLayout(new LC().fill().hideMode(2)));
		add(jpConnection, new CC().growX().wrap());
		add(new JScrollPane(jtaConsole), new CC().width("500:100%:").height("200:100%:").wrap());
		add(jlLastExec, new CC().alignX("right").wrap());
		add(jpTestsOperations, new CC().grow());

		pack();
		setMinimumSize(getSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Teste Memcached");
		setLocationRelativeTo(null);
		
		jpTestsOperations.setVisible(false);
		scrollValue.setVisible(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				MemcachedPoolUtils.shutDownPool();
			}
		});
	}

	private void flushAll() {
		try {
			if(JOptionPane.showConfirmDialog(this, "Clear ALL caches keys and values?", "Warning", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION){
				return;
			}
			MemCachedClient mcc = MemcachedPoolUtils.newClientFromPool();
			jtaConsole.setText("FLUSH ALL: "+mcc.flushAll(new String[]{address+":"+port}));
		} catch (Exception e) {
			e.printStackTrace();
			jtaConsole.setText("ERROR FLUSH ALL! \n"+e.getMessage());
		} finally {
			finishTest();
		}
	}

	private void showStats() {
		try {
			MemCachedClient mcc = MemcachedPoolUtils.newClientFromPool();
			Map<String, Map<String, String>> stats = mcc.stats();
			if(stats==null || stats.isEmpty()){
				jtaConsole.setText("No information available for: "+address+":"+port);
			}else{
				Map<String, String> serverStats = stats.get(address+":"+port);
				
				long memBytes = convertLong(serverStats.get("bytes"));
				long maxMemBytes = convertLong(serverStats.get("limit_maxbytes"));
				float p = (memBytes*100f)/maxMemBytes;
				DecimalFormat df = new DecimalFormat("###,##0.00");
				
				jtaConsole.setText("MEMORY BYTES: \nUsed -> "+(memBytes/1024)+" kb / "+(maxMemBytes/1024/1024)+" mb ("+df.format(p)+"%)\n\n");
				jtaConsole.append("SERVER STATS: \n"+serverStats);
				jtaConsole.setCaretPosition(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jtaConsole.setText("ERROR STATS! \n"+e.getMessage());
		} finally {
			finishTest();
		}
	}
	
	private long convertLong(String str) {
		return str==null||str.trim().isEmpty()?0:Long.parseLong(str.trim());
	}

	private void testGetSet() {
		try {
			MemCachedClient mcc = MemcachedPoolUtils.newClientFromPool();
			
			String key = jtfKey.getText();
			if(key.isEmpty()){
				JOptionPane.showMessageDialog(this, "KEY IS EMPTY!", "Messsage", JOptionPane.WARNING_MESSAGE);
				jtfKey.requestFocus();
				return;
			}
			if(jcbOption.getSelectedIndex()==0){
				jtaConsole.setText("GET: \n"+mcc.get(key));
			}else{
				String value = jtaValue.getText();
				if(value.isEmpty()){
					JOptionPane.showMessageDialog(this, "VALUE IS EMPTY!", "Message", JOptionPane.WARNING_MESSAGE);
					jtaValue.requestFocus();
					return;
				}
				jtaConsole.setText("SET: \n"+mcc.set(key, value));
			}
			
			jpTestsOperations.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			jtaConsole.setText("ERROR TEST GET/SET! \n"+e.getMessage());
		} finally{
			finishTest();
		}
	}

	private void testConnection() {
		jbTestConnection.setEnabled(false);
		jpTestsOperations.setVisible(false);
		jtaConsole.setText("Testing connection...\n\n");
		address = jtfIpServer.getText();
		port = (Integer)jsPort.getValue();
		jtaConsole.append("ADDRESS: "+address+"\n");
		jtaConsole.append("PORT:    "+port+"\n\n");
		new SwingWorker<Boolean, Void>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				MemCachedClient mcc = MemcachedPoolUtils.initializePool(true, address, port);
				boolean retSet = mcc.set("key", "VALUE TEST");
				String retGet = (String) mcc.get("key");
				return retSet && retGet!=null && !retGet.isEmpty();
			}
			@Override
			protected void done() {
				try {
					if(get()){
						jtaConsole.replaceRange("Connection SUCCESS!!",0, jtaConsole.getText().indexOf("\n"));
						jpTestsOperations.setVisible(true);
					}else{
						jtaConsole.replaceRange("Connection FAILURE!!",0, jtaConsole.getText().indexOf("\n"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					jtaConsole.replaceRange("CONNECTION ERROR! \n"+e.getMessage(),0, jtaConsole.getText().indexOf("\n"));
				} finally{
					jbTestConnection.setEnabled(true);
					finishTest();
				}
			}
		}.execute();
	}

	private void finishTest() {
		jlLastExec.setText(SDF.format(new Date()));
		contentPaneUpdateUI();
	}
	
	private void contentPaneUpdateUI() {
		((JPanel)getContentPane()).updateUI();
	}

	public static void main(String[] args) throws Exception{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new ViewTest().setVisible(true);
	}
}
