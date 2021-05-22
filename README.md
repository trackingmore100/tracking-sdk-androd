Trackingmore-android
=================

The Android SDK of Trackingmore API

## Official document

[Document](https://www.trackingmore.com/v3/api-index)

Quick Start
--------------

Tips:
- When sandbox and lang are included in your parameters, it will be automatically added to the parameters. If sandbox is true, your request will not require the correct APIKEY, but the results returned by each interface will be fixed
- Declare your API KEY as a static final variable
- Every time you want to make a request, create and execute it right away.
- Don't reuse request.
- Always control the Exceptions in the result. And be careful with the casts.
- Implement in your activites AsyncTaskCompleteListener<ConnectionAPI>, that will force to create the method
  onTaskComplete(ConnectionAPI result), is where the Asynchronous return will be send.
- All the Constructors of methods has as parameters:
    - ApiKey: To link to that account.
    - ConnectionAPIMethods: To specific with action do in the account.
    - Listener: To tell the method what to execute when it finished, usually is "this", so the method at complexion will
      execute onTaskComplete of the this class.
- All the returns are a com.google.gson.JsonObject class, you can add that in your grade dependencies:
  implementation'com.google.code.gson:gson:2.8.6'


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

## Typical Server Responses

We will respond with one of the following status codes.

Code|Type | Message
----|--------------|-------------------------------
200    | <code>Success</code>|    Request response is successful
203    | <code>PaymentRequired</code>|  API service is only available for paid account Please subscribe paid plan to unlock API services                                                             ul
204    | <code>No Content</code>|    Request was successful, but no data returned Tracking NO. or target data possibly do not exist
400    | <code>Bad Request</code>| Request type error Please check the API documentation for the request type of this API
401    | <code>Unauthorized</code>|    Authentication failed or has no permission Please check and ensure your API Key is correct
403    | <code>Bad Request</code>|    Page does not exist Please check and ensure your link is correct                                                                                             ul
404    | <code>Not Found</code>|    Page does not exist Please check and ensure your link is correct
408    | <code>Time Out</code>|    Request timeout The official website did not return data, please try again later
411    | <code>Bad Request</code>|    Specified request parameter length exceeds length limit Please check and ensure that the request parameters are of the required length
412    | <code>Bad Request</code>|    Specified request parameter format doesn't meet requirements Please check and ensure that the request parameters are in the required format
413    | <code>Out limited</code>|    The number of request parameters exceeds the limit Please check the API documentation for the limit of this API
417    | <code>Bad Request</code>|    Missing request parameters or request parameters cannot be parsed Please check and ensure that the request parameters are complete and correctly formatted
421    | <code>Bad Request</code>|    Some of required parameters are empty Some couriers need special parameters to track logistics information (Special Couriers)
422    | <code>Bad Request</code>|    Unidentifiable courier code Please check and ensure that the courier code are correct(Courier code)
423    | <code>Bad Request</code>|    Tracking No. already exists
424    | <code>Bad Request</code>|    Tracking No. no exists Please use 「Create trckings」 API first to create trackings
429    | <code>Bad Request</code>|    Exceeded API request limits, please try again later Please check the API documentation for the limit of this API
511    | <code>Server Error</code>|    Server error Please contact us: service@trackingmore.org.
512    | <code>Server Error</code>|    Server error Please contact us: service@trackingmore.org.
513    | <code>Server Error</code>|    Server error Please contact us: service@trackingmore.org.                                                                                                    
