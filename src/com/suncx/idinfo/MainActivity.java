package com.suncx.idinfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	private EditText et;
	private TextView tvSex;
	private TextView tvBirth;
	private TextView tvAddr;
	public static final int SHOW_RESPONSE=0;
	public static final int ERROR=1;
	public static final String APPKEY="2f43b65957065344f302ac21ac59b9fc";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EditText et=(EditText) findViewById(R.id.et);
		Button bt=(Button) findViewById(R.id.bt);
		
		
		
		bt.setOnClickListener(this);
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_RESPONSE:
				String res=(String)msg.obj;
				//String area;
				//String sex;
				//String birthday;
				//parseJSONWithJSONObject(res);
				try{
					JSONObject jsonObj=new JSONObject(res);
					int errorCode=jsonObj.getInt("error_code");
					String reason=jsonObj.getString("reason");
					JSONObject resultObj= jsonObj.getJSONObject("result");
					String area=resultObj.getString("area");
					String sex=resultObj.getString("sex");
					String birthday=resultObj.getString("birthday");
					tvSex.setText(sex);
					tvBirth.setText(birthday);
					tvAddr.setText(area);
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			case ERROR:
				Toast.makeText(MainActivity.this, "Ê§°Ü", Toast.LENGTH_SHORT).show();
			default:
				break;
				
			}
		}
	};
	
	public void onClick(View v){
		String cardno=et.getText().toString().trim();
		final String path="http://apis.juhe.cn/idcard/index?cardno="+cardno+"&dtype=json&key="+APPKEY;
		//sendRequestWithHttpURLConnection();
		new Thread(new Runnable(){
			public void run(){
				HttpURLConnection conn=null;
				try{
					URL url = new URL(path);
					conn=(HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					if(conn.getResponseCode()==200){
						InputStream is=conn.getInputStream();
						BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
						String line=null;
						StringBuilder response=new StringBuilder();
						while((line=br.readLine())!=null){
							response.append(line);
						}
						Message message=new Message();
						message.obj=response.toString();
						message.what=SHOW_RESPONSE;
						handler.sendMessage(message);
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(conn!=null){
						conn.disconnect();
					}
				}
				
			}
		}).start();
	}
	
	private void parseJSONWithJSONObject(String data){
		//public static String area;
		try{
			JSONObject jsonObj=new JSONObject(data);
			int errorCode=jsonObj.getInt("error_code");
			String reason=jsonObj.getString("reason");
			JSONObject resultObj= jsonObj.getJSONObject("result");
			String area=resultObj.getString("area");
			String sex=resultObj.getString("sex");
			String birthday=resultObj.getString("birthday");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
