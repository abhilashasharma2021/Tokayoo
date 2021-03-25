package com.tokayoapp.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ChasingDots
import com.rilixtech.Country
import com.rilixtech.CountryCodePicker
import com.rilixtech.CountryCodePicker.OnCountryChangeListener
import com.tokayoapp.R
import com.tokayoapp.Utils.API
import com.tokayoapp.Utils.AppConstant
import com.tokayoapp.Utils.GenericTextWatcher
import kotlinx.android.synthetic.main.activity_mobile_o_t_p.*
import org.json.JSONException
import org.json.JSONObject

class MobileOTPActivity() : AppCompatActivity() {
    var editOne: EditText? = null
    var editTwo: EditText? = null
    var editThree: EditText? = null
    var editFour: EditText? = null
    var editMobile: EditText? = null
    var txtHintEnter: TextView? = null
    var txt_counter: TextView? = null
    var strs = ""
    var strsp = ""
    var strspl = ""
    var strsplite = ""
    var strcombine: String? = null
    var newOtp: String? = null
    var strMobile: String? = ""
    var strUserId: String? = ""
    var progress: ProgressBar? = null
    var progressNew: ProgressBar? = null
    var ll_verify: LinearLayout? = null
    var ll_otp: LinearLayout? = null
    var relVerify: RelativeLayout? = null
    var relsend: RelativeLayout? = null
    var rl_count: RelativeLayout? = null
    var counntryPicker: CountryCodePicker? = null
    var selected_country_code: String? = null
    var txtresend: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_o_t_p)
        relVerify = findViewById(R.id.relVerify)
        counntryPicker = findViewById(R.id.counntryPicker)
        relsend = findViewById(R.id.relsend)
        txt_counter = findViewById(R.id.txt_counter)
        rl_count = findViewById(R.id.rl_count)
        txtHintEnter = findViewById(R.id.txtHintEnter)
        progress = findViewById(R.id.progress)
        txtresend = findViewById(R.id.txtresend)
        ll_otp = findViewById(R.id.ll_otp)
        editMobile = findViewById(R.id.editMobile)
        progressNew = findViewById(R.id.progressNew)
        txtresend?.setOnClickListener(View.OnClickListener { resend_Otp() })
        relVerify?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                if (validateFields()) {
                    newOtp = editOne?.text.toString() + editTwo?.text.toString() + editThree?.text.toString() + editFour?.text.toString()
                    Log.e("ygfhf", "onClick: " + newOtp)
                    mobile_varification()
                } else {
                    validateFields()
                }


            }
        })
        relsend?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                strMobile = editMobile?.text!!.toString()
                if ((editMobile?.text.toString() == "")) {
                    Toast.makeText(this@MobileOTPActivity, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
                } else {
                    signUp()
                   // genrate_otp()
                }
            }
        })
        editOne = findViewById(R.id.editOne)
        editTwo = findViewById(R.id.editTwo)
        editThree = findViewById(R.id.editThree)
        editFour = findViewById(R.id.editFour)

        txtresend?.visibility = View.GONE

        val edit = arrayOf<EditText>(editOne!!, editTwo!!, editThree!!, editFour!!)
        editOne!!.addTextChangedListener(GenericTextWatcher(edit, editOne!!));
        editTwo!!.addTextChangedListener(GenericTextWatcher(edit, editTwo!!));
        editThree!!.addTextChangedListener(GenericTextWatcher(edit, editThree!!));
        editFour!!.addTextChangedListener(GenericTextWatcher(edit, editFour!!));


        val countrycode = counntryPicker?.getDefaultCountryCodeWithPlus().toString()
        selected_country_code = countrycode
        Log.e("dsdjsb", countrycode)
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE)
        val editor = AppConstant.sharedpreferences.edit()
        editor.putString(AppConstant.SelectedCountryCode, selected_country_code)
        editor.commit()
        counntryPicker?.setOnCountryChangeListener(object : OnCountryChangeListener {
            override fun onCountrySelected(country: Country) {
                selected_country_code = counntryPicker?.getSelectedCountryCodeWithPlus()
                Log.e("ygiyhgiuui", counntryPicker?.getSelectedCountryCodeWithPlus())
            }
        })
    }


    fun validateFields(): Boolean {
        var boolean: Boolean
        if (editOne?.text.isNullOrEmpty()) {
            editOne?.error = "Required"
            boolean = false
        } else if (editTwo?.text.isNullOrEmpty()) {
            editTwo?.error = "Required"
            boolean = false
        } else if (editThree?.text.isNullOrEmpty()) {
            editThree?.error = "Required"
            boolean = false
        } else if (editFour?.text.isNullOrEmpty()) {
            editFour?.error = "Required"
            boolean = false
        } else {
            boolean = true
        }

        return boolean
    }
    fun resend_Otp() {
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE)
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Id, "")
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.UserMobile, "")
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=resend_otp")
        progress!!.visibility = View.VISIBLE
        AndroidNetworking.post(API.BASEURL + API.resend_otp)
                .addBodyParameter("id", strUserId)
                .addBodyParameter("mobile", strMobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("sdkls", response.toString())
                        try {
                            if ((response.getString("result") == "Otp Sent Successfully")) {
                                timer()


                            };
                            progress!!.visibility = View.GONE
                        } catch (e: JSONException) {
                            Log.e("efer", e.message)
                            progress!!.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.e("ewdwefd", anError.message)
                        progress!!.visibility = View.GONE
                    }
                })
    }
    private fun timer() {
        txtresend!!.text = "Otp Sent Successfully"
        txt_counter?.visibility = View.VISIBLE
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txt_counter!!.text = "Resend Code : " + millisUntilFinished / 1000
                txtresend?.setOnClickListener { Toast.makeText(this@MobileOTPActivity, "Wait for ${millisUntilFinished / 1000} seconds", Toast.LENGTH_SHORT).show() }
            }

            override fun onFinish() {
                txtresend!!.text = "Resend Otp"
                txtresend?.setOnClickListener { resend_Otp() }
                txt_counter?.visibility = View.GONE

                //  txt_counter!!.text = "Done!!!!"
            }
        }.start()
    }
    fun genrate_otp() {
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE)
        val strUserID = AppConstant.sharedpreferences.getString(AppConstant.Id, "")
        progress!!.visibility = View.VISIBLE
        Log.e("sfdsfg", strMobile)
        AndroidNetworking.post(API.BASEURL + API.genrate_otp)
                .addBodyParameter("mobile", "$selected_country_code-$strMobile")
                .addBodyParameter("id", strUserID)
                .setPriority(Priority.HIGH)
                .setTag("Please wait")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("fghhfg", response.toString())
                        try {
                            if ((response.getString("result") == "Otp Sent Successfully")) {
                                txtresend?.visibility = View.VISIBLE
                                val otp = response.getString("otp")
                                val id = response.getString("id")
                                val mobile = response.getString("mobile")
                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE)
                                val editor = AppConstant.sharedpreferences.edit()
                                editor.putString(AppConstant.UserMobile, mobile)
                                //  editor.putString(AppConstant.UserId, id);
                                editor.putString(AppConstant.OTP, otp)
                                editor.commit()


                                // ll_verify.setVisibility(View.VISIBLE);
                                txtHintEnter!!.visibility = View.VISIBLE
                                ll_otp!!.visibility = View.VISIBLE
                                editOne!!.visibility = View.VISIBLE
                                editTwo!!.visibility = View.VISIBLE
                                editThree!!.visibility = View.VISIBLE
                                editFour!!.visibility = View.VISIBLE
                                relsend!!.visibility = View.GONE
                                relVerify!!.visibility = View.VISIBLE
                                rl_count!!.visibility = View.VISIBLE

                                object : CountDownTimer(60000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        txt_counter!!.text = "Resend Code : " + millisUntilFinished / 1000
                                        txtresend?.setOnClickListener { Toast.makeText(this@MobileOTPActivity, "Wait for ${millisUntilFinished / 1000} seconds", Toast.LENGTH_SHORT).show() }
                                    }

                                    override fun onFinish() {
                                        txt_counter?.visibility = View.GONE
                                        txtresend?.setOnClickListener { resend_Otp() }

                                        //  txt_counter!!.text = "Done!!!!"
                                    }
                                }.start()


/*
                                editOne.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        editTwo.requestFocus();
                                    }
                                });
                                editTwo.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        editThree.requestFocus();

                                    }
                                });
                                editThree.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        editFour.requestFocus();

                                    }
                                });
                                editFour.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        strs = editOne.getText().toString().trim();
                                        Log.e("fgfgfg", strs);
                                        strsp = editTwo.getText().toString().trim();
                                        Log.e("fgfgfg", strsp);
                                        strspl = editThree.getText().toString().trim();
                                        Log.e("fgfgfg", strspl);
                                        strsplite = editFour.getText().toString().trim();
                                        Log.e("fgfgfg", strsplite);
                                        strcombine = strs + strsp + strspl + strsplite;
                                        Log.e("dkjfdfdljd", strcombine);

                                    }
                                });*/

                                // Toast.makeText(MobileOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                progress!!.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MobileOTPActivity, response.getString("result"), Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            Log.e("dhfghg", e.message)
                            progress!!.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.e("reytrhfhgf", anError.message)
                        progress!!.visibility = View.GONE
                    }
                })
    }

    fun mobile_varification() {
        progressNew!!.visibility = View.VISIBLE

        Log.e("jkjgfglg", selected_country_code + strMobile)
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=mobile_varification")
        AndroidNetworking.post(API.BASEURL + API.mobile_varification)
                .addBodyParameter("mobile", "$selected_country_code-$strMobile")
                .addBodyParameter("otp", newOtp)
                .setPriority(Priority.HIGH)
                .setTag("Please wait")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("dfsgfsgfdffh", response.toString())
                        try {
                            if ((response.getString("result") == "Mobile Verfication successfully")) {

                                Toast.makeText(this@MobileOTPActivity, response.getString("result"), Toast.LENGTH_SHORT).show()
                                //val id = response.getString("id")
                              val mobile = response.getString("contact")

                             /*   val id = response.getString("id")
                                val username = response.getString("username")
                                val email = response.getString("email")
                                val password = response.getString("password")
                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, MODE_PRIVATE)
                                val editor = AppConstant.sharedpreferences.edit()
                                editor.putString(AppConstant.Id, id)
                                editor.putString(AppConstant.UserName, username)
                                editor.putString(AppConstant.UserEmail, email)
                                editor.commit()*/


                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, MODE_PRIVATE)
                                val strUserID = AppConstant.sharedpreferences.getString(AppConstant.Id, "")


                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE)
                                val editor2 = AppConstant.sharedpreferences.edit()
                                editor2.putString(AppConstant.UserMobile, mobile)
                                editor2.putString(AppConstant.UserId, response.getString("id"))
                                editor2.commit()
                                startActivity(Intent(this@MobileOTPActivity, MainActivity::class.java))
                                Animatoo.animateZoom(this@MobileOTPActivity)
                                finishAffinity()
                                progressNew!!.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MobileOTPActivity, response.getString("result"), Toast.LENGTH_SHORT).show()
                                progressNew!!.visibility = View.GONE
                            }
                        } catch (e: JSONException) {
                            Log.e("dfgdg", e.message)
                            progressNew!!.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.e("tryghf", anError.message)
                        progressNew!!.visibility = View.GONE
                    }
                })
    }


    ////////// /*USING FIREBASE SAVE ""REGDID"" END HERE ON LOGIN*///////////////////////////////////////////////
    fun signUp() {
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, MODE_PRIVATE)
        val  signupEmail = AppConstant.sharedpreferences.getString(AppConstant.signupEmail, "")
        val  nickName = AppConstant.sharedpreferences.getString(AppConstant.nickName, "")
        val  signupRegID = AppConstant.sharedpreferences.getString(AppConstant.signupRegID, "")
        val  signupPassword = AppConstant.sharedpreferences.getString(AppConstant.signupPassword, "")
        spin_kit.setVisibility(View.VISIBLE)
        val chasingDots: Sprite = ChasingDots()
        spin_kit.setIndeterminateDrawable(chasingDots)

        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=signup")
        AndroidNetworking.post(API.BASEURL + API.RegisterUser)
                .addBodyParameter("regid", signupRegID)
                .addBodyParameter("os_type", "1")
                .addBodyParameter("username", nickName)
                .addBodyParameter("email", signupEmail)
                .addBodyParameter("mobile", "$selected_country_code-$strMobile")
                .addBodyParameter("password", signupPassword)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        spin_kit.setVisibility(View.GONE)
                        Log.e("dsfdv", response.toString())
                        try {
                            if (response.getString("result") == "Otp send successfully") {
                                txtresend?.visibility = View.VISIBLE
                                //val otp = response.getString("otp")
                               // val id2 = response.getString("id")
                               // val mobile = response.getString("mobile")
                               /* AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE)
                                val editor2 = AppConstant.sharedpreferences.edit()
                                editor2.putString(AppConstant.UserMobile, mobile)
                                editor.putString(AppConstant.UserId, id);
                                editor2.putString(AppConstant.OTP, otp)
                                editor2.commit()*/
                                Toast.makeText(this@MobileOTPActivity, response.getString("result") , Toast.LENGTH_SHORT).show()

                                txtHintEnter!!.visibility = View.VISIBLE
                                ll_otp!!.visibility = View.VISIBLE
                                editOne!!.visibility = View.VISIBLE
                                editTwo!!.visibility = View.VISIBLE
                                editThree!!.visibility = View.VISIBLE
                                editFour!!.visibility = View.VISIBLE
                                relsend!!.visibility = View.GONE
                                relVerify!!.visibility = View.VISIBLE
                                rl_count!!.visibility = View.VISIBLE

                                object : CountDownTimer(60000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        txt_counter!!.text = "Resend Code : " + millisUntilFinished / 1000
                                        txtresend?.setOnClickListener { Toast.makeText(this@MobileOTPActivity, "Wait for ${millisUntilFinished / 1000} seconds", Toast.LENGTH_SHORT).show() }
                                    }

                                    override fun onFinish() {
                                        txt_counter?.visibility = View.GONE
                                        txtresend?.setOnClickListener { resend_Otp() }
                                        //  txt_counter!!.text = "Done!!!!"
                                    }
                                }.start()

                                mobile_varification()


                                Toast.makeText(this@MobileOTPActivity, response.getString("result"), Toast.LENGTH_SHORT).show()
                            } else {
                                Log.i("gfhgfhg", "onResponse: " + response.getString("result"))
                                Toast.makeText(this@MobileOTPActivity, response.getString("result"), Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            Log.e("rtrtt", e.message)
                            spin_kit.setVisibility(View.GONE)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.e("yujyhj", anError.message)
                        spin_kit.setVisibility(View.GONE)
                    }
                })
    }



}