package org.techtown.boggle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static String TAG = "netTest2_MapActivity";
    LatLng mapCam = new LatLng(37.568764704274805, 126.97885619508857);
    GoogleMap map;
    static ArrayList<String[]> allPlace = new ArrayList<String[]>();

    protected void onCreate(Bundle savedInstanceState) {
        GetData task = new GetData();
        task.execute("http://116.89.189.17/connect.php");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        View hB = findViewById(R.id.how);
        hB.setOnClickListener(new View.OnClickListener() {
            //설명 다이얼로그를 띄웁니다.
            @Override
            public void onClick(View v) {
                teach();
            }
        });
        EditText sm = findViewById(R.id.searchMap);
        View smb = findViewById(R.id.searchMapButton);
        smb.setOnClickListener(new View.OnClickListener() {
            //맵에서 검색
            @Override
            public void onClick(View v) {
                // 검색창에서 텍스트를 가져온다
                String searchText = sm.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocationName(searchText, 3);
                    if (addresses != null && !addresses.equals(" ")) {
                        Address address = addresses.get(0);
                        mapCam = new LatLng(address.getLatitude(),address.getLongitude());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                mapCam,16   // 위도, 경도, 줌
                        ));
                    }
                } catch(Exception e) {

                }
            }
        });
        View lB = findViewById(R.id.goList);
        lB.setOnClickListener(new View.OnClickListener() {
            //PlayActivity로 이동합니다.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
            }
        });

        //Toast.makeText(getApplicationContext(), Integer.toString(placeInfos.size()) ,Toast.LENGTH_SHORT).show();

        SupportMapFragment mF = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mF.getMapAsync(this);

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

            progressDialog = ProgressDialog.show(MapActivity.this,
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
                    //웹에서 읽어온 값들 allPlace에 저장
                    for(int i = 0; i < leng; i++){
                        jjj = ja.getJSONObject(i);
                        String[] sa = {jjj.getString("doh"),jjj.getString("si"),jjj.getString("gun"),
                                jjj.getString("gu"),jjj.getString("place"),jjj.getString("nowN"),
                                jjj.getString("ruleN"),jjj.getString("updateTime"),jjj.getString("notice"),
                                jjj.getString("lat"),jjj.getString("lng")};
                        allPlace.add(sa);
                    }
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
    public void teach(){
        View dialogView = getLayoutInflater().inflate(R.layout.howtouse, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // ↑매개변수로 GoogleMap 객체가 넘어옵니다.
        // 전역변수에 구글맵 대입
        map = googleMap;
        // camera 좌표를 경동대 근처로 옮겨 봅니다.
        // 구글지도(지구) 에서의 zoom 레벨은 1~23 까지 가능합니다.
        // 여러가지 zoom 레벨은 직접 테스트해보세요
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                mapCam,16   // 위도, 경도
        ));
        for(int i = 0; i < allPlace.size(); i++){
            // marker 표시
            // market 의 위치, 타이틀, 짧은설명 추가 가능.
            MarkerOptions marker = new MarkerOptions();
            marker
                    .position(new LatLng(Double.parseDouble(allPlace.get(i)[9]), Double.parseDouble(allPlace.get(i)[10])))
                    .title(allPlace.get(i)[4])
                    .snippet("현재인원 = "+allPlace.get(i)[5]);
            Double Nratio = Double.parseDouble(allPlace.get(i)[5])/Double.parseDouble(allPlace.get(i)[6]);
            if(Nratio<0.333333){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else if(Nratio<0.666666){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }else{
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            googleMap.addMarker(marker); // 마커추가,화면에출력

            // 마커클릭 이벤트 처리
            // GoogleMap 에 마커클릭 이벤트 설정 가능.
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    // 마커정보창 클릭시 호출되는 콜백 메서드
                    Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                    for(int j = 0; j < allPlace.size(); j++){
                        if(allPlace.get(j)[4].equals(marker.getTitle())){
                            intent.putExtra("info",allPlace.get(j));
                        }
                    }
                    startActivity(intent);
                }
            });
        }
    }
    public void onBackPressed(){
        ActivityCompat.finishAffinity(this);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}