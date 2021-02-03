package com.tokayoapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tokayoapp.Adapter.ChatAdapter;
import com.tokayoapp.Modal.ChatModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class ChatActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    RecyclerView rec_chat;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    RecyclerView.LayoutManager layoutManager;
    ChatAdapter chatAdapter;
   public static EditText edt_typeMsg;
   public static ImageView img_send;
    String strMsg="",strUserId="";
    ArrayList<ChatModal> chatArrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId=AppConstant.sharedpreferences.getString(AppConstant.UserId,"");
        Log.e("fhdfjk",strUserId);

        rl_back=findViewById(R.id.rl_back);
        img_send=findViewById(R.id.img_send);
        edt_typeMsg=findViewById(R.id.edt_typeMsg);
        rec_chat=findViewById(R.id.rec_chat);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               strMsg=edt_typeMsg.getText().toString().trim();
               if (strMsg.equals("")){

                   Toast.makeText(ChatActivity.this, "Type a message", Toast.LENGTH_SHORT).show();
               }else{

                   type_chat();
               }

            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("guytguuygf","");
                ShowChat();
            }
        };

        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rec_chat.setLayoutManager(layoutManager);
        rec_chat.setHasFixedSize(true);

    }

    public void type_chat(){

        Log.e("tytuj",strUserId);
        Log.e("tytuj",strMsg);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=message_insert")
        AndroidNetworking.post(API.BASEURL+API.message_insert)
                .addBodyParameter("sender_id",strUserId)
                .addBodyParameter("reciver_id","1")
                .addBodyParameter("message",strMsg)
                .setTag("Type Msg")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dkjflkd",response.toString());
                        try {
                            if (response.getString("result").equals("success")){
                              edt_typeMsg.setText("");
                                ShowChat();

                            }
                        } catch (JSONException e) {
                           Log.e("fhdshj",e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("hbgfjv", anError.getMessage() );
                    }
                });





    }



    public void ShowChat(){
/*params-sender id means user id ,receiver id, msg type--text or image, time and date*/
      //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=chat_list")
        AndroidNetworking.post(API.BASEURL+API.chat_list)
                .addBodyParameter("sender_id",strUserId)
                .addBodyParameter("reciver_id","1")
                .setTag("Show chat")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("dfhjhvc",response.toString());
                        chatArrayList= new ArrayList<>();

                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String m_id=jsonObject.getString("m_id");
                                    String sender_id=jsonObject.getString("sender_id");
                                    String reciver_id=jsonObject.getString("reciver_id");
                                    String date=jsonObject.getString("date");
                                    String time=jsonObject.getString("time");
                                    String message=jsonObject.getString("message");
                                    String path=jsonObject.getString("path");
                                    String message_image=jsonObject.getString("message_image");
                                    String message_video=jsonObject.getString("message_video");
                                    String thambanil_img=jsonObject.getString("thambanil_img");
                                    String filepath=jsonObject.getString("filepath");
                                    String audio=jsonObject.getString("audio");
                                    String file=jsonObject.getString("file");

                                    ChatModal chatModal=new ChatModal();
                                    chatModal.setSender_id(jsonObject.getString("sender_id"));
                                    chatModal.setReceiver_id(jsonObject.getString("reciver_id"));
                                    chatModal.setId(jsonObject.getString("m_id"));
                                    chatModal.setImage(jsonObject.getString("thambanil_img"));
                                    chatModal.setMessage(jsonObject.getString("message"));
                                    chatModal.setTime(jsonObject.getString("time"));
                                    chatModal.setDay(jsonObject.getString("date"));
                                    chatArrayList.add(chatModal);

                                }


                                chatAdapter=new ChatAdapter(ChatActivity.this,chatArrayList);
                                rec_chat.scrollToPosition(chatArrayList.size()-1);
                                rec_chat.setAdapter(chatAdapter);

                            }catch (JSONException e) {
                                Log.e("dsfjdkj",e.getMessage());
                            }

                        }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("ytiuyoiu", anError.getMessage() );
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowChat();

        registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("FBR-IMAGE"));
    }


    @Override
    protected void onPause() {

        unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
    }

}
