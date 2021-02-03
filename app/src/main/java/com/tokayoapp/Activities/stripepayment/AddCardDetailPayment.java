package com.tokayoapp.Activities.stripepayment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.tokayoapp.Activities.PaymentMode;
import com.tokayoapp.Activities.PurchaseHistoryActivity;
import com.tokayoapp.Activities.RedemptionHistoryActivity;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCardDetailPayment extends AppCompatActivity {
    Button save;
    CardMultilineWidget cardMultilineWidget;
    String strTotalPrice = "";
    TextView txtPrice;
    ProgressBar spin_kit;
    String st_paymentStatus = "", st_user_id = "", st_TotalAmount = "", st_address_id = "", st_CheckoutStatus = "";
    String st_RewardId = "", st_RewardPoints = "", strMobile = "", strFullName = "", strAddress = "";
    String st_WeightId = "", st_ColorId = "", st_ModelId = "";
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_card_detail_payment);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, 0);
        strTotalPrice = AppConstant.sharedpreferences.getString(AppConstant.CartGrandTotal, "");
        st_TotalAmount = AppConstant.sharedpreferences.getString(AppConstant.CartGrandTotal, "");
        st_paymentStatus = AppConstant.sharedpreferences.getString(AppConstant.paymentStatus, "");
        st_user_id = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        st_address_id = AppConstant.sharedpreferences.getString(AppConstant.defaultIdAddress, "");
        st_CheckoutStatus = AppConstant.sharedpreferences.getString(AppConstant.CheckoutStatus, "");
        st_RewardId = AppConstant.sharedpreferences.getString(AppConstant.RewardId, "");
        st_RewardPoints = AppConstant.sharedpreferences.getString(AppConstant.singleRewardPoints, "");
        strFullName = AppConstant.sharedpreferences.getString(AppConstant.RewardUserName, "");
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.RewardUserMobile, "");
        strAddress = AppConstant.sharedpreferences.getString(AppConstant.RewardUserAddress, "");
        st_ModelId = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedModelId, "");
        st_ColorId = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedColorId, "");
        st_WeightId = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedWeightId, "");

        dialog = new ProgressDialog(AddCardDetailPayment.this);

        txtPrice = findViewById(R.id.txtPrice);
        txtPrice.setText("Total Price" + " " + strTotalPrice);
        cardMultilineWidget = findViewById(R.id.card_input_widget);
        save = findViewById(R.id.save_payment);
        spin_kit = findViewById(R.id.spin_kit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard();
            }
        });

    }

    private void saveCard() {

        Card card = cardMultilineWidget.getCard();

        if (card == null) {
            Toast.makeText(getApplicationContext(), "Invalid card", Toast.LENGTH_SHORT).show();
        } else {
            if (!card.validateCard()) {
                // Do not continue token creation.
                Toast.makeText(getApplicationContext(), "Invalid card", Toast.LENGTH_SHORT).show();
            } else {
                CreateToken(card);
            }
        }
    }

    private void CreateToken(final Card card) {

        dialog.setMessage("please wait..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Stripe stripe = new Stripe(getApplicationContext(), getString(R.string.publishablekey));
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        // Send token to your server
                        Log.e("kshvkjsbv", token.getId());
                        Intent intent = new Intent();
                        intent.putExtra("card", token.getCard().getLast4());
                        intent.putExtra("stripe_token", token.getId());
                        intent.putExtra("cardtype", token.getCard().getBrand());
                        setResult(0077, intent);
                        Log.e("kshvkjsbv", card.getNumber());
                        Log.e("kshvkjsbv", card.getCVC());
                        Log.e("kshvkjsbv", card.getExpMonth() + "");
                        Log.e("kshvkjsbv", card.getExpYear() + "");

                        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                        editor.putString(AppConstant.CardNumber, card.getNumber());
                        editor.putString(AppConstant.CardExpMonth, card.getExpMonth() + "");
                        editor.putString(AppConstant.CardExpYear, card.getExpYear() + "");
                        editor.putString(AppConstant.CardCCV, card.getCVC());
                        editor.putString(AppConstant.Token, token.getId());
                        editor.commit();

                        // chekout_product();
                        CheckPayApp(card.getNumber(), card.getCVC(), card.getExpMonth() + "", card.getExpYear() + "", token.getId());

                        //finish();
                    }

                    public void onError(Exception error) {

                        // Show localized error message
                        dialog.dismiss();
                        Log.e("ijofokvkb", error.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getLocalizedMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void CheckPayApp(String number, String cvc, String s, String s1, String id) {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        AndroidNetworking.post("https://3511535117.co/Tokayo/api/stripe/app/payment.php")
                .addBodyParameter("cardNumber", number)
                .addBodyParameter("cardCVC", cvc)
                .addBodyParameter("cardExpMonth", s)
                .addBodyParameter("cardExpYear", s1)
                .addBodyParameter("source", id)
                .addBodyParameter("amount", strTotalPrice)
                .addBodyParameter("currency", "MYR")
                .addBodyParameter("method", "charge")
                .setTag("Payment")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("sfsdfsdffs", response.toString());
                        try {
                            if (response.getString("response").equals("Success")) {
                                if (st_CheckoutStatus.equals("OrderProduct")) {
                                    chekout_product();
                                } else {
                                    checkout_Reward();
                                }
                            }
                            else {
                                dialog.dismiss();
                                spin_kit.setVisibility(View.GONE);
                                Toast.makeText(AddCardDetailPayment.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex) {
                            dialog.dismiss();
                            spin_kit.setVisibility(View.GONE);
                            Log.e("sdgsdgsg", ex.getMessage());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.e("sdgsdgsg", anError.toString());
                    }
                });

    }

    public void chekout_product() {


        Log.e("rtytyt", st_user_id);
        Log.e("rtytyt", st_paymentStatus);
        Log.e("rtytyt", st_TotalAmount);
        Log.e("rtytyt", st_address_id);

        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=checkout")
        AndroidNetworking.post(API.BASEURL + API.checkout)
                .addBodyParameter("user_id", st_user_id)
                .addBodyParameter("address_id", st_address_id)
                .addBodyParameter("total_amount", st_TotalAmount)
                .addBodyParameter("order_type", st_paymentStatus)
                .setTag("Order")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("trtyghh", response.toString());

                        try {

                            if (response.getString("result").equals("Placed order successfully")) {

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String order_id = response.getString("order_id");
                                String total_amount = response.getString("total_amount");
                                String status = response.getString("status");
                                String address_id = response.getString("address_id");
                                String rewards_point = response.getString("rewards_point");
                                Log.e("dldf", id);
                                Log.e("dldf", address_id);
                                Log.e("dldf", order_id);
                                Log.e("dldf", total_amount);
                                Log.e("dldf", rewards_point);
                                dialog.dismiss();
                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.order_no, order_id);
                                editor.putString(AppConstant.CartGrandTotal, total_amount);
                                editor.putString(AppConstant.RewardPoints, rewards_point);
                                editor.commit();
                                spin_kit.setVisibility(View.GONE);

                                Toast.makeText(AddCardDetailPayment.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                final Dialog dialog = new Dialog(AddCardDetailPayment.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.paymentsucessful);


                                RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.rl);

                                rl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(AddCardDetailPayment.this, PurchaseHistoryActivity.class));
                                        finish();
                                    }
                                });
                                dialog.show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(AddCardDetailPayment.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                spin_kit.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            dialog.dismiss();
                            Log.e("sdlksfk", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.e("trytr", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }


    public void checkout_Reward() {

     /*   spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);*/

        Log.e("dgfdvg", st_user_id);
        Log.e("dgfdvg", st_paymentStatus);
        Log.e("dgfdvg", st_RewardId);
        Log.e("dgfdvg", st_RewardPoints);
        Log.e("dgfdvg", strFullName);
        Log.e("dgfdvg", strMobile);
        Log.e("dgfdvg", "sadfasfa" + st_WeightId);

        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=reward_checkout")
        AndroidNetworking.post(API.BASEURL + API.reward_checkout)
                .addBodyParameter("user_id", st_user_id)
                .addBodyParameter("product_id", st_RewardId)
                .addBodyParameter("color_id", st_ColorId)
                .addBodyParameter("model_id", st_ModelId)
                .addBodyParameter("weight", st_WeightId)
                .addBodyParameter("username", strFullName)
                .addBodyParameter("total_point", st_RewardPoints)
                .addBodyParameter("order_type", st_paymentStatus)
                .addBodyParameter("order_status", "0")
                .addBodyParameter("contact", strMobile)
                .addBodyParameter("address", strAddress)
                .setTag("Reward Checkout")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("gdfgg", response.toString());

                        try {
                            if (response.getString("result").equals("Placed order successfully")) {
                                spin_kit.setVisibility(View.GONE);

                                dialog.dismiss();

                                final Dialog dialog = new Dialog(AddCardDetailPayment.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.rewardssucessful);
                                dialog.dismiss();
              /*  TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText(ok);*/

                                RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.rl);
                                rl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(AddCardDetailPayment.this, RedemptionHistoryActivity.class));
                                        Animatoo.animateZoom(AddCardDetailPayment.this);
                                        finish();
                                    }
                                });

                                dialog.show();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Log.e("dgfvdg", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.e("fgdgf", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });
    }
}