Trackingmore-android
=================

The Android SDK of Trackingmore API

## This project is looking for maintainers

If you would like to be a maintainer of this project, please reach out through our public slack channel [Trackingmore Contract Us](https://www.trackingmore.com/sales.html#ContactUs) to express your interest. Thanks in advance for your help!


Quick Start
--------------

Tips:

- Declare your API KEY as a static final variable
- Every time you want to make a request, create and execute it right away.
- Don't reuse request.
- Always control the Exceptions in the result. And be careful with the casts.
- Implement in your activites AsyncTaskCompleteListener<ConnectionAPI>, that will force to create the method
  onTaskComplete(ConnectionAPI result), is where the Asynchronous return will be send.
- All the Constructors of methods has as parameters:
    - ApiKey: To link to that account.
    - ConnectionAPIMethods: To specific with action do in the account.
    - Listener: To tell the method what to execute when it finished, usually is "this", so the method at complexion
      will execute onTaskComplete of the this class.
- All the returns are a com.google.gson.JsonObject class, you can add that in your grade dependencies: implementation'com.google.code.gson:gson:2.8.6'


**Get a list of supported couriers**

    static final String APIKEY ="test";

    //We can call the method from onCreated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConnectionAPI(APIKEY, ConnectionAPIMethods.couriersList, this).execute();
    }
    
    //define the method onTaskComplete in your Activity
    public void onTaskComplete(ConnectionAPI result) {
    
        //Control the exception of the result
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
        //Every method has a number associate, couriersList is 0
        //method Enum Code
        System.out.println(result.getMethod().getNumberMethod());
        //prettyJson and deal request result
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalResult = gson.toJson(result.getReturn());

        tv_main.setText(finalResult);
        tv_main.setTextSize(14);
        tv_main.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    **Get a list of the couriers in Trackingmore**

	new ConnectionAPI(APIKEY, ConnectionAPIMethods.couriersList, this).execute();
	
	public void onTaskComplete(ConnectionAPI result) {
        if(result.getMethod().getNumberMethod()==0){
            //prettyJson and deal request result
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String finalResult = gson.toJson(result.getReturn());
    
            tv_main.setText(finalResult);
            tv_main.setTextSize(14);
            tv_main.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
	}

    **Detect which couriers defined in your account match a tracking number**
    //Do this call whenever you want
    String params = "{'sandbox':true,'tracking_number': 'EA152563254CN'}";
    JsonObject json = JsonParser.parseString(params).getAsJsonObject();
    new ConnectionAPI(APIKEY, ConnectionAPIMethods.couriersDetect, this, json).execute();

    public void onTaskComplete(ConnectionAPI result) {
        //Remember to control a possible Exception
    
        if(result.getMethod().getNumberMethod()==2){
            //prettyJson and deal request result
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String finalResult = gson.toJson(result.getReturn());
    
            tv_main.setText(finalResult);
            tv_main.setTextSize(14);
            tv_main.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }


    **Post a tracking to your account**

	//First we have to create a Tracking params
	String params = "{'sandbox':true,'tracking_number': 'EA152563254CN', 'carrier_code': 'china-ems'}";
    JsonObject json = JsonParser.parseString(params).getAsJsonObject();

	//add the tracking to our account
    new ConnectionAPI(APIKEY, ConnectionAPIMethods.createTrackings, this, json).execute();

    public void onTaskComplete(ConnectionAPI result) {
        //Remember to control a possible Exception
    
        if(result.getMethod().getNumberMethod()==3){
            //prettyJson and deal request result
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String finalResult = gson.toJson(result.getReturn());
    
            tv_main.setText(finalResult);
            tv_main.setTextSize(14);
            tv_main.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }


**Summary of Connection API Methods with all the api and Methods**


    public void onConfirmApi(TextView textView) {
        //Other business codes start //
        Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
        hideSoftKeyboard(this);
        //Other business codes end //
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
        }
    }
