package com.tokayoapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tokayoapp.Fcm.Config;
import com.tokayoapp.Fcm.NotificationUtils;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{

    TextView txtDontHaveAccount,txtForgot;
    EditText emailUserName,editpass;
    String strEmail="",strPassword="";
    ProgressBar spin_kit;
    RelativeLayout rl_signup;
    ImageView imgGoogle,imgFacebook;
///////////////////////GOOGLE INTEGRATION////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton sign_in_button;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////*FACEBOOK*//////////////////////////////////////////////////////////////////////
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    LoginButton login_button;

    private void displayMessage(Profile profile) {

            /*if(profile != null){
                textView.setText(profile.getName());
            }*/
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////REGID//////////////////////////////////////////////////////////////
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String strregId = "";
    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RelativeLayout relSignIn=findViewById(R.id.relSignIn);


        txtDontHaveAccount = findViewById(R.id.txtDontHaveAccount);
        emailUserName = findViewById(R.id.emailUserName);
        editpass = findViewById(R.id.editpass);
        txtForgot = findViewById(R.id.txtForgot);
        rl_signup = findViewById(R.id.rl_signup);
        spin_kit = findViewById(R.id.spin_kit);
        imgGoogle = findViewById(R.id.imgGoogle);
        imgFacebook = findViewById(R.id.imgFacebook);
        login_button = findViewById(R.id.login_button);
        sign_in_button = findViewById(R.id.sign_in_button);
     /*   SpannableString content = new SpannableString(getResources().getString(R.string.dont_have_account));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 22, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtDontHaveAccount.setText(content);*/

        // /////////////// ///////////////// /* FACEBOOK START(1) ON HERE*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        accessTokenTracker =new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        imgFacebook=findViewById(R.id.imgFacebook);
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_button.performClick();
            }
        });

        profileTracker = new ProfileTracker(){
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

/////////////////////////////////// /* FACEBOOK END(1) ON HERE*////////////////////////////////////////////////////////////////////////


        rl_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                Animatoo.animateZoom(LoginActivity.this);

            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswod.class));
                Animatoo.animateZoom(LoginActivity.this);
            }
        });

        relSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail=emailUserName.getText().toString().trim();
                strPassword=editpass.getText().toString().trim();


                if (strEmail.equals("")){

                    Toast.makeText(LoginActivity.this,"Please Enter Email",Toast.LENGTH_LONG).show();

                }

                else if (strPassword.equals("")){

                    Toast.makeText(LoginActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();

                }
                else
                {
                  logIn();
                }

            }
        });

/////////////////////*Google Intregation login without firebase start here(1)*//////////////////////////////////////////////////////////

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


/////////////////////*Google Intregation login without firebase end here(1)*//////////////////////////////////////////////////////////



        ////////////////////////// //*USING FIREBASE SAVE ""REGDID"" ON LOGIN*///////////////////////////////////////////////

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };
        /*using firebase save  regid on login screen*/
        displayFirebaseRegId();




    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        strregId = pref.getString("regId", null);
        Log.e("gtfrhgfh", "Firebase reg id: " + strregId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    ////////// /*USING FIREBASE SAVE ""REGDID"" END HERE ON LOGIN*///////////////////////////////////////////////


    /////////////////////*Google Intregation login without firebase start here(2)*//////////////////////////////////////////////////////////
    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {


        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String authId = account.getId();

            Log.e("kdfjldk", "Google sign" + account.getId());

            SocialLogin(account.getId(),account.getEmail(),"Google");
            // Signed in successfully, show authenticated UI.
            Toast.makeText(LoginActivity.this, "Successfully Login In Google", Toast.LENGTH_LONG).show();
           // startActivity(new Intent(LoginActivity.this, MainActivity.class));


            //   updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signIn Error", "signInResult:failed code=" + e.getStatusCode());
            //  updateUI(null);
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        super.onStart();
    }



/////////////////////*Google Intregation login without firebase END here(2)*//////////////////////////////////////////////////////////

    public void logIn() {
        spin_kit.setVisibility(View.VISIBLE);
       Sprite chasingDots = new ChasingDots();
       spin_kit.setIndeterminateDrawable(chasingDots);

       Log.e("dfgf", strEmail);
        Log.e("dfgf", strPassword);
       // Log.e("dfgf", strregId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=login")
       AndroidNetworking.post(API.BASEURL+API.loginUser)
                .addBodyParameter("email", strEmail)
               .addBodyParameter( "regid",strregId )
               .addBodyParameter( "os_type","1" )
                .addBodyParameter("password", strPassword)
                .setPriority(Priority.HIGH)
                .setTag("Please wait")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spin_kit.setVisibility(View.GONE); // monu ne kiya ye visible rehta tha
                        Log.e("dsgdfdsg",response.toString());
                        try {
                            if (response.getString("result").equals("Login SuccessFully")) {

                                String id=response.getString("id");
                                String username=response.getString("username");
                                String email=response.getString("email");
                                String password=response.getString("password");
                                String contact=response.getString("contact");
                                Log.e("dfkldk",response.getString("id"));
                                Log.e("dskv;ldsk", contact );/*+60-896767668*/

                                String[] splitStr = contact.split("-");
                                String selected_country_code=splitStr[0];
                                String split_otwo=splitStr[0];
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Animatoo.animateZoom(LoginActivity.this);
                                Toast.makeText(LoginActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.UserId,id);
                                editor.putString(AppConstant.SelectedCountryCode,selected_country_code);
                                editor.putString(AppConstant.UserName, username);
                                editor.putString(AppConstant.UserEmail, email);
                                editor.putString(AppConstant.UserMobile, response.getString("contact"));
                                editor.commit();




                            }
                            else {
                                Toast.makeText(LoginActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("dfgdfgfbl", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("defdfdfbl", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }

    public void SocialLogin(String id, String email, String google) {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        Log.e("dvkcvclkx",id);
        Log.e("dvkcvclkx",email);
        Log.e("dvkcvclkx",google);
        AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=social_login")
                .addBodyParameter("auth_id",id)
                .addBodyParameter("email",email)
                .addBodyParameter("auth_provider",google)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override

                    public void onResponse(JSONObject response) {
                      Log.e("tyhytghn",response.toString());

                        try {
                            if (response.has("result")) {

                                if (response.getString("result").equals("Login Successfully.")) {
                                    spin_kit.setVisibility(View.GONE);
                                    String id=response.getString("id");
                                    String username=response.getString("username");
                                    String email=response.getString("email");
                                    Log.e("hcxjh",id);
                                    Log.e("hcxjh",username);
                                    Log.e("hcxjh",email);

                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.UserId, response.getString("id"));
                                    editor.putString(AppConstant.UserName, response.getString("username"));
                                    editor.putString(AppConstant.UserEmail, response.getString("email"));
                                    editor.putString(AppConstant.RewardPoints, response.getString("reward_point"));
                                    editor.commit();

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Animatoo.animateZoom(LoginActivity.this);
                                    finish();
                                } else {
                                    spin_kit.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("sdfshgdfjsdf", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rherhdsh", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);

                    }
                });
    }
}