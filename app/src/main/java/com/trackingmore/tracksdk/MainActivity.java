package com.trackingmore.tracksdk;

import Classes.*;
import Enums.ConnectionAPIMethods;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener<ConnectionAPI> {
    //此字符串是要在下拉菜单中显示的列表项
    private static final String[] ApiList = new String[]{"couriersList", "modifyCourier",
            "couriersDetect", "createTrackings", "getTrackings", "modifyInfo", "deleteTrackings", "stopUpdate", "remote", "status", "transitTime", "realtime", "airCargo", "getUserInfo"};
    private static final String DocumentUrl = "https://www.trackingmore.com/v3/api-index";

    static final String APIKEY = "test";
    private static final String TAG = "MainActivity";
    TextView tv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_main = findViewById(R.id.tv_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "You can  click the logo of the apk to jump document url.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        final AutoCompleteTextView textView = findViewById(R.id.api_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.select_menu, ApiList);
        textView.setAdapter(adapter);
        Button button = findViewById(R.id.confirm_button);
        button.setOnClickListener(view -> onConfirmApi(textView));
    }

    public void onConfirmApi(TextView textView) {
        Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
        hideSoftKeyboard(this);
        String method = textView.getText().toString();
        String params = "{'sandbox':true,'tracking_number': 'EA152563254CN', 'carrier_code': 'china-ems'}";
        JsonObject json = JsonParser.parseString(params).getAsJsonObject();
        switch (method) {
            case "couriersList":
                params = "{\"sandbox\":true,\"lang\":\"cn\"}";
                json = JsonParser.parseString(params).getAsJsonObject();
                new ConnectionAPI(APIKEY, ConnectionAPIMethods.resolve(method), this, json).execute();
                break;
            case "modifyCourier":
                params = "{'sandbox':true,'tracking_number': 'EA152563254CN', 'courier_code': 'china-ems', 'new_courier_code': 'china-post'}";
                json = JsonParser.parseString(params).getAsJsonObject();
                new ConnectionAPI(APIKEY, ConnectionAPIMethods.resolve(method), this, json).execute();
                break;
            case "couriersDetect":
                params = "{'sandbox':true,'tracking_number': 'EA152563254CN'}";
                json = JsonParser.parseString(params).getAsJsonObject();
                new ConnectionAPI(APIKEY, ConnectionAPIMethods.resolve(method), this, json).execute();
                break;
            case "createTrackings":
                new ConnectionAPI(APIKEY, ConnectionAPIMethods.resolve(method), this, json).execute();
                break;
            case "getTrackings":
                new ConnectionAPI(APIKEY, ConnectionAPIMethods.resolve(method), this, json).execute();
                break;
            case "modifyInfo":
                new ConnectionAPI(APIKEY, ConnectionAPIMethods.resolve(method), this, json).execute();
                break;
            case "deleteTrackings":
                break;
            case "stopUpdate":
                break;
            case "remote":
                break;
            case "status":
                break;
            case "transitTime":
                break;
            case "realtime":
                break;
            case "airCargo":
                break;
            case "getUserInfo":
                break;
            default:
                Toast toast = Toast.makeText(MainActivity.this, "None Api Selected", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
        }
    }


    public void onTaskComplete(ConnectionAPI result) {
        if (result.getException() != null) {
            Log.v(TAG, "Log.v output log info", result.getException());
            String message = result.getException().getMessage();
            Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            tv_main.setText(message);
            tv_main.setTextSize(14);
            tv_main.setTextColor(ContextCompat.getColor(this, R.color.black));
            return;
        }
        // Toast::makeText(result.getException().getMessage());//do something with the exception)
        //method Enum Code
        System.out.println(result.getMethod().getNumberMethod());
        //prettyJson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalResult = gson.toJson(result.getReturn());

        tv_main.setText(finalResult);
        tv_main.setTextSize(14);
        tv_main.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    public void onLogoClick(View view) {
        Uri uri = Uri.parse(DocumentUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public static void hideSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}