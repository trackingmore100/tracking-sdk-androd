package Classes;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import Enums.ConnectionAPIMethods;
import android.app.ProgressDialog;
import android.content.Context;

import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.json.JSONTokener;

/**
 * ConnectionAPI is the class responsible of the iteration with the HTTP API of Trackingmore, it wrap all the
 * funcntionalities in different methods
 * Created by User on 10/6/14
 */
public class ConnectionAPI extends AsyncTask<Void, Void, ConnectionAPI> {

    private static final String URL_SERVER = "http://api.trackingmore.com/";
    private static String VERSION_API = "v3";
    private final String ApiKey;

    /**
     * callback
     */
    private final AsyncTaskCompleteListener<ConnectionAPI> callback;
    /**
     * mehtod call
     */
    private final ConnectionAPIMethods method;
    private final ProgressDialog dialog;
    private Boolean Sandbox = false;
    private JsonObject params = new JsonObject();
    private Exception exception;
    private JsonObject returnData;
    private String lang = "en";

    //methods only Android SDK

    /**
     * Constructor with the basic information, only can be called internally
     *
     * @param method   Which method as an action (getTracking, deleteTrackings...)
     * @param callback Object where execute the callback
     * @param ApiKey   KEY API link to the user account
     **/
    private ConnectionAPI(ConnectionAPIMethods method, AsyncTaskCompleteListener<ConnectionAPI> callback, String ApiKey) {
        this.method = method;
        this.callback = callback;
        this.ApiKey = ApiKey;
        /*show the dialog from the context from where is called, if error, delete this line*/
        this.dialog = callback instanceof Context ? new ProgressDialog((Context) callback) : null;
    }

    /**
     * Constructor for couriersList and getAllCouriers
     *
     * @param keyAPI   KEY API link to the user account
     * @param method   Which method as an action (getTracking, deleteTracking...)
     * @param callback Object where execute the callback
     **/
    public ConnectionAPI(String keyAPI, ConnectionAPIMethods method, AsyncTaskCompleteListener<ConnectionAPI> callback) {
        this(method, callback, keyAPI);
        if (method != ConnectionAPIMethods.couriersList)
            this.exception = new RequestAPIException("The constructor only can be called with ConnectionAPIMethods.couriersList");

    }

    /**
     * Constructor for modifyCouriers
     *
     * @param keyAPI   KEY API link to the user account
     * @param method   Which method as an action (getTracking, deleteTracking...)
     * @param callback Object where execute the callback
     **/
    public ConnectionAPI(String keyAPI, ConnectionAPIMethods method, AsyncTaskCompleteListener<ConnectionAPI> callback, JsonObject params) {
        this(method, callback, keyAPI);
        this.params = params;
    }

    /**
     * Before calling the request we show a message with this information
     **/
    protected void onPreExecute() {
        if (dialog != null) {
            this.dialog.setMessage("Calling API");
            this.dialog.show();
        }
    }

    /**
     * In this method we declare the code that we want to run asynchronous
     **/
    protected ConnectionAPI doInBackground(Void... params) {
        //couriersList(0),modifyCourier(1),couriersDetect(2),createTrackings(3)
        // ,getTrackings(4),modifyInfo(5),deleteTrackings(6),stopUpdate(7),remote(8),status(9)
        // ,transitTime(10),realtime(11),getUserInfo(12),airCargo(13);
        if (this.exception == null) {
            try {
                if (!this.params.isJsonNull() && this.params.has("sandbox") && this.params.get("sandbox").getAsBoolean()) {
                    Sandbox = true;
                    this.params.remove("sandbox");
                }
                if (!this.params.isJsonNull() && this.params.has("lang")) {
                    this.lang = this.params.get("lang").getAsString();
                }
                switch (this.method.getNumberMethod()) {
                    case 0://couriersList
                        this.returnData = this.couriersList();
                        break;
                    case 1: //modifyCourier
                        this.returnData = this.modifyCourier();
                        break;
                    case 2://couriersDetect
                        this.returnData = this.couriersDetect();
                        break;
                    case 3://createTrackings
                        this.returnData = this.createTrackings();
                        break;
                    case 4://getTrackings
                        this.returnData = this.getTrackings();
                        break;
                    case 5://modifyInfo
                        this.returnData = this.modifyInfo();
                        break;
                    case 6://deleteTrackings
                        this.returnData = this.deleteTrackings();
                        break;
                    case 7://stopUpdate(7)
                        this.returnData = this.stopUpdate();
                        break;
                    case 8://remote(8)
                        this.returnData = this.remote();
                        break;
                    case 9://status(9)
                        this.returnData = this.status();
                        break;
                    case 10://transitTime(10)
                        this.returnData = this.transitTime();
                        break;
                    case 11://realtime(11)
                        this.returnData = this.realtime();
                        break;
                    case 12://getUserInfo(13)
                        this.returnData = this.getUserInfo();
                        break;
                    case 13://airCargo(13)
                        this.returnData = this.airCargo();
                        break;
                }
            } catch (Exception e) {
                this.exception = e;
            }

        }
        return this;
    }

    /**
     * Return a list of couriers supported by Trackingmore along with their names,
     * URLs and slugs
     *
     * @return A list of Object Courier, with all the couriers supported by the API
     * @throws RequestAPIException If the request response an error
     * @throws IOException         If there is a problem with the connection
     * @throws ParseException      If the response can not be parse to JsonObject
     **/
    public JsonObject couriersList() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("GET", "courier?lang=" + lang, null);
    }

    public JsonObject modifyCourier() throws RequestAPIException, IOException, ParseException, JsonParseException {
        JsonObject body = new JsonObject();
        if (params.isJsonNull() || !params.has("tracking_number") ||  params.get("tracking_number").isJsonNull())
            throw new RequestAPIException("the tracking number should be always informed for the method modifyCourier");
        if (params.isJsonNull() || !params.has("courier_code") || params.get("courier_code").isJsonNull())
            throw new RequestAPIException("the old courier code should be always informed for the method modifyCourier");
        if (params.isJsonNull() ||  !params.has("new_courier_code") ||  params.get("new_courier_code").isJsonNull())
            throw new RequestAPIException("the new courier code should be always informed for the method modifyCourier");
        System.out.println(params);
        body.addProperty("tracking_number", params.get("tracking_number").getAsString());
        body.addProperty("courier_code", params.get("courier_code").getAsString());
        body.addProperty("new_courier_code", params.get("new_courier_code").getAsString());
        body.addProperty("lang",lang);
        return this.request("PUT", "modifycourier", body);
    }

    /**
     * Get a list of matched couriers for a tracking number based on the tracking number format
     * Note, only check the couriers you have defined in your account
     *
     * @return A List of Couriers objects that match the provided trackingNumber
     * @throws RequestAPIException if the request response an error
     *                             Invalid JSON data. If the tracking number doesn't match any courier defined in your account,
     *                             or it doesn't match any courier defined in Trackingmore
     * @throws IOException         If there is a problem with the connection
     * @throws ParseException      If the response can not be parse to JsonObject
     **/
    public JsonObject couriersDetect()
            throws RequestAPIException, IOException, ParseException, JsonParseException {

        JsonObject body = new JsonObject();
        if (params.isJsonNull() ||  !params.has("tracking_number") ||  params.get("tracking_number").isJsonNull())
            throw new RequestAPIException("the tracking number should be always informed for the method couriersDetect");
        body.addProperty("tracking_number", params.get("tracking_number").getAsString());
        body.addProperty("lang",lang);
        return this.request("POST", "detect", body);
    }
    public JsonObject createTrackings() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("POST", "create", params);
    }

    public JsonObject getTrackings() throws RequestAPIException, IOException, ParseException, JsonParseException {
        QueryString qs = new QueryString();
        qs.add("created_date_min",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("created_date_max",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("shipping_date_min",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("shipping_date_max",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("updated_date_min",params.has("updated_date_min")? DateMethods.dateToStamp(params.get("updated_date_min").getAsString()) :null);
        qs.add("updated_date_max",params.has("updated_date_max")? DateMethods.dateToStamp(params.get("updated_date_max").getAsString()) :null);
        qs.add("lang",lang);
        qs.add("pages_amount",params.has("pages_amount")?params.get("pages_amount").getAsString():null);
        qs.add("items_amount",params.has("items_amount")?params.get("items_amount").getAsString():null);
        qs.add("tracking_numbers",params.has("tracking_numbers")?params.get("tracking_numbers").getAsString():null);
        qs.add("courier_code",params.has("courier_code")?params.get("courier_code").getAsString():null);
        qs.add("order_numbers",params.has("order_numbers")?params.get("order_numbers").getAsString():null);
        qs.add("delivery_status",params.has("delivery_status")?params.get("delivery_status").getAsString():null);
        qs.add("archived_status",params.has("archived_status")?params.get("archived_status").getAsString():null);
        String query = qs.toString().replaceFirst("&","?");
        return this.request("GET", "get"+query, null);
    }

    public JsonObject modifyInfo() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("PUT", "create", params);
    }

    public JsonObject deleteTrackings() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("DELETE", "delete", params);
    }

    public JsonObject stopUpdate() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("POST", "notupdate", params);
    }

    public JsonObject remote() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("POST", "remote", params);
    }

    public JsonObject status() throws RequestAPIException, IOException, ParseException, JsonParseException {
        QueryString qs = new QueryString();
        qs.add("created_date_min",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("created_date_max",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("shipping_date_min",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("shipping_date_max",params.has("created_date_min")? DateMethods.dateToStamp(params.get("created_date_min").getAsString()) :null);
        qs.add("lang",lang);
        qs.add("courier_code",params.has("courier_code")?params.get("courier_code").getAsString():null);
        String query = qs.toString().replaceFirst("&","?");
        return this.request("GET", "status"+query, null);
    }

    public JsonObject realtime() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("POST", "realtime", params);
    }

    public JsonObject transitTime() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("POST", "transittime", params);
    }

    public JsonObject getUserInfo() throws RequestAPIException, IOException, ParseException, JsonParseException {
        return this.request("GET", "userinfo?lang="+lang,null);
    }

    public JsonObject airCargo() throws RequestAPIException, IOException, ParseException, JsonParseException {
        VERSION_API = "v2";
        return this.request("POST", "aircargo", params);
    }

    /**
     * Call the callback and dismiss the dialog
     **/
    protected void onPostExecute(ConnectionAPI result) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        callback.onTaskComplete(result);
    }

    /**
     * Return an Object depending of what method was used to create the object.
     **/
    public JsonObject getReturn() {
        return returnData;
    }

// end of methods only Android



    /**
     * make a request to the HTTP API of Trackingmore
     *
     * @param method String with the method of the request: GET, POST, PUT, DELETE
     * @param url    String with the URL of the request
     * @param body   JsonObject with the body of the request, if the request doesn't need body "GET/DELETE", the body
     *               would be null
     * @return A JsonObject with the response of the request
     * @throws RequestAPIException If the request response an error
     * @throws IOException         If there is a problem with the connection
     * @throws ParseException      If the response can not be parse to JsonObject
     **/
    public JsonObject request(String method, String url, JsonObject body)
            throws RequestAPIException, IOException, ParseException, JsonParseException {
        BufferedReader rd;
        StringBuilder sb;
        OutputStreamWriter wr;

        HttpURLConnection connection;
        URL serverAddress;
        if (Sandbox) {
            serverAddress = new URL(new URL(URL_SERVER), VERSION_API + "/trackings/sandbox/" + url);
        } else {
            serverAddress = new URL(new URL(URL_SERVER), VERSION_API + "/trackings/" + url);
        }
        System.out.println("request Url :" + serverAddress);
        connection = (HttpURLConnection) serverAddress.openConnection();
        connection.setRequestMethod(method);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        if (VERSION_API.equals("V2")) {
            connection.setRequestProperty("Trackingmore-Api-Key", ApiKey);
        }else{
            connection.setRequestProperty("Tracking-Api-Key", ApiKey);
        }
        System.out.println("apiKey:" + ApiKey);
        if (body != null) {
            connection.setDoOutput(true);
        }//if there is information in body, doOutput true, to write

        connection.connect();
        if (body != null) {
            wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body.toString());
            wr.flush();
        }

        this.checkAPIResponse(connection.getResponseCode(), connection);
        rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return JsonParser.parseString(sb.toString()).getAsJsonObject();
    }

    /**
     * Check the status of a http response and if the status is an error throws an exception
     *
     * @param status Status of the connection response
     * @throws RequestAPIException A customize exception with a message
     *                             depending of the status error
     **/
    public void checkAPIResponse(int status, HttpURLConnection connection)
            throws RequestAPIException, IOException, ParseException, JsonParseException {

        if (status > 201) {
            BufferedReader rd;
            StringBuilder sb;
            String message = "";
            String type = "";
            rd = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }
            throw new RequestAPIException((type + ". " + message + " " + sb).trim());
        }

    }

    public Exception getException() {
        return exception;
    }

    public ConnectionAPIMethods getMethod() {
        return method;
    }


}
