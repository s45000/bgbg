package org.techtown.boggle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.boggle.InfoActivity;
import org.techtown.boggle.MainActivity;
import org.techtown.boggle.R;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    private static String TAG = "netTest2_MainActivity";
    static String[] CitySql = {"","","",""};
    ListView rA;
    static ArrayList<String[]> allPlace = new ArrayList<String[]>();
    static ArrayList<String[]> PlaceInfos = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //일단 전체데이터 읽어옴(지도 바로 이용 가능)
        PlaceInfos.clear();
        GetData task = new GetData();
        task.execute("http://116.89.189.17/connect.php");

        rA = (ListView)findViewById(R.id.resultArea);

        View mB = findViewById(R.id.mapButton);
        mB.setOnClickListener(new View.OnClickListener() {
            //MapActivity로 이동합니다.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                //intent.putExtra("place",allPlace);
                startActivity(intent);
            }
        });

        View sB = findViewById(R.id.searchButton);
        sB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.searchWhere);
                String userWant = editText.getText().toString();

                String[] uW = userWant.split(" ");
                if(inputRight(uW)) {
                    PlaceInfos.clear();
                    //클라우드db의 웹서버에서 값들 PlaceInfos[][]에 저장;
                    GetData task = new GetData();
                    //onPreExecute()
                    //doInBackground
                    //onPostExecute() 순으로 실행
                    //onPostExecute()에서 PlaceInfos에 읽어온 데이터 싸그리 입력
                    task.execute("http://116.89.189.17/connect.php");
                }else{
                    Toast.makeText(getApplicationContext(), "행정구역을 올바르게 입력해주세요.(도,시,군,구 검색가능", Toast.LENGTH_SHORT).show();
                }
            }
        });
        View reb = findViewById(R.id.refresh);
        reb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reFresh();
            }
        });
    }
    void reFresh(){
        finish();//인텐트 종료
        Intent intent2 = getIntent(); //인텐트
        startActivity(intent2); //액티비티 열기
    }
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PlayActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response  - " + result);

            if (result == null){
                //토스트로 에러날리기
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            }else {
                //더미데이터에 읽어온 값들 넣기
                try {
                    JSONObject jj = null;
                    jj = new JSONObject(result);
                    JSONArray ja = new JSONArray(jj.getString("Info"));
                    JSONObject jjj = null;
                    int leng = Integer.parseInt(jj.getString("Leng"));
                    //웹에서 읽어온 값들 PlaceInfos에 저장
                    for(int i = 0; i < leng; i++){
                        jjj = ja.getJSONObject(i);
                        String[] sa = {jjj.getString("doh"),jjj.getString("si"),jjj.getString("gun"),
                                jjj.getString("gu"),jjj.getString("place"),jjj.getString("nowN"),
                                jjj.getString("ruleN"),jjj.getString("updateTime"),jjj.getString("notice"),
                                jjj.getString("lat"),jjj.getString("lng")};
                        PlaceInfos.add(sa);
                        allPlace.add(sa);
                    }
                    //여기서 검색한 도시군구 정보만 남기고 PlaceInfos에서 삭제!
                    for(int i = 0; i < 4; i++) {
                        if (!(CitySql[i].equals(""))) {
                            for (int j = 0; j < PlaceInfos.size(); j++) {
                                if (!(PlaceInfos.get(j)[i].equals(CitySql[i]))) {
                                    PlaceInfos.remove(j);
                                    j--;
                                }
                            }
                        }
                    }
                    //결과값 리스트뷰로 출력
                    SearchRight();
                }catch(JSONException e){e.printStackTrace();}

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }
    public void  SearchRight(){ // 결과값 리스트뷰로 출력
        //Toast.makeText(getApplicationContext(), CitySql[0]+" "+CitySql[1]+" "+CitySql[2]+" "+CitySql[3], Toast.LENGTH_SHORT).show();
        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data){
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                Double nowN = Double.parseDouble(PlaceInfos.get(position)[5]);
                Double ruleN = Double.parseDouble(PlaceInfos.get(position)[6]);
                if((nowN/ruleN)<0.333333) {
                    //view.setBackgroundColor(Color.rgb(163,204,163));
                    view.setBackgroundResource(R.drawable.level1);
                }else if((nowN/ruleN)<0.666666) {
                    //view.setBackgroundColor(Color.rgb(255,212,0));
                    view.setBackgroundResource(R.drawable.level2);
                }else{
                    //view.setBackgroundColor(Color.rgb(163,0,0));
                    view.setBackgroundResource(R.drawable.level3);
                }
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        rA.setAdapter(adapter);
        //리스트뷰 아이템 클릭시
        rA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //pos번째값을 infoActivity로 넘겨줍니다.
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("info",PlaceInfos.get(pos));
                startActivity(intent);
            }
        });

        for(int i = 0; i < PlaceInfos.size(); i++) {
            data.add(PlaceInfos.get(i)[4]);
        }
        adapter.notifyDataSetChanged();

    }
    public boolean inputRight(String[] txt){
        String[] CitySection = {"도", "시", "군", "구"};
        for(int i = 0; i < 4; i++)
            CitySql[i]="";
        String[] UserInput = txt;
        int numCount = 4;
        if(UserInput.length>4)
            return false;
        for(int i = 0; i < UserInput.length; i++){
            if(UserInput[i].equals(""))
                return false;
            for(int j = 0; j < 4; j++){
                if(UserInput[i].substring(UserInput[i].length()-1).equals(CitySection[j]))
                    CitySql[j] = UserInput[i].substring(0, UserInput[i].length()-1);
            }
        }
        for(int i = 0; i < 4; i++){
            if(CitySql[i].equals(""))
                numCount--;
        }
        if(numCount==UserInput.length)
            return true;
        return false;
    }
    public void onBackPressed(){
        ActivityCompat.finishAffinity(this);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}