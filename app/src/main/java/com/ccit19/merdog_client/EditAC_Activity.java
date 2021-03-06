package com.ccit19.merdog_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccit19.merdog_client.backServ.AppController;
import com.ccit19.merdog_client.backServ.BackPressed;
import com.ccit19.merdog_client.backServ.SaveSharedPreference;
import com.ccit19.merdog_client.backServ.urlset;
import com.ccit19.merdog_client.databinding.ActivityEditAcBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditAC_Activity extends AppCompatActivity {
    ActivityEditAcBinding binding;
    BackPressed backPressed =new BackPressed(this);

    String cert_number;
    private static final int MILLISINFUTURE = 180 * 1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private int count = 180;
    private CountDownTimer countDownTimer;

    boolean pwState=false;
    boolean pwconState=false;
    boolean nameState=false;
    boolean phonenumState=false;
    boolean phonecheckState=false;
    String url_ = urlset.getInstance().getData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ac_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_edit_ac_);
        binding.setActivity(this);

        setSupportActionBar(binding.eaToolbar);

        // ???????????? ???????????? ?????????
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ???????????? ???????????? ???????????? ??????(?????? ???????????? ???????????? ?????????)
        getSupportActionBar().setTitle("?????? ??????");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_24px);

        if(SaveSharedPreference.getLoginType(getApplicationContext())!=0){
            binding.eaMainForm.setVisibility(View.GONE);
        }

        EditACDataHolder holder=new EditACDataHolder();

        String url = url_ + "/userapp/mypage";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest loadInfoForm = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        boolean success= false;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            success = jsonObject.getBoolean("result");
                            if (success){
                                holder.name=jsonObject.getString("user_name");
                                holder.phonenum=jsonObject.getString("user_phone");
                                binding.eaEdtID.setText(SaveSharedPreference.getUserID(getApplicationContext()).toString());
                                binding.eaEdtName.setText(jsonObject.getString("user_name"));
                                binding.eaEdtPhone.setText(jsonObject.getString("user_phone"));
                            }else {
                                Toast.makeText(getApplicationContext(),"???????????? ??????.\n????????? ?????? ????????? ?????????",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????."+error.getMessage(),Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),"?????????????????????.\n???????????? ?????? ??????????????????.",Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????.",Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        })
        {
            @Override
            public  Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", SaveSharedPreference.getUserIdx(getApplicationContext()).toString()); //???
                return params;
            }
        };
        loadInfoForm.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(loadInfoForm);
        //???????????? ?????????
        binding.eaEdtPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#$%^&*])[a-zA-Z0-9~!@#$%^&*]{5,15}$";
                Matcher matcher = Pattern.compile(pwPattern).matcher(s);

                pwPattern = "(.)\\1\\1\\1";
                Matcher matcher2 = Pattern.compile(pwPattern).matcher(s);

                if (!matcher.matches()) {
                    binding.eaTxvPassAlt.setText("??????, ??????, ???????????? ?????? 5~15????????? ??????????????????.");
                    pwState = false;
                } else if (matcher2.find()) {
                    binding.eaTxvPassAlt.setText("??????????????? 4????????? ???????????? ????????????.");
                    pwState = false;
                } else if (s.toString().contains(" ")) {
                    binding.eaTxvPassAlt.setText("????????? ????????? ??????????????????.");
                    pwState = false;
                } else {
                    binding.eaTxvPassAlt.setText("");
                    pwState = true;
                }
            }
        });
        //???????????? ?????? ?????????
        binding.eaEdtConPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.eaEdtPW.getText().toString().equals(s.toString()) &&!s.toString().isEmpty()) {
                    binding.eaTxvPassconAlt.setText("??????????????? ???????????????.");
                    binding.eaTxvPassconAlt.setTextColor(Color.parseColor("#5CAB7D"));
                    pwconState = true;
                }else {
                    binding.eaTxvPassconAlt.setText("??????????????? ???????????? ????????????.");
                    binding.eaTxvPassconAlt.setTextColor(Color.parseColor("#E53A40"));
                    pwconState = false;
                }
            }
        });
        //????????? ?????????
        binding.eaEdtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(holder.name)){
                    binding.eaBtnNamecheck.setEnabled(false);
                    nameState=false;
                }else {
                    binding.eaBtnNamecheck.setEnabled(true);
                    String nicPattern = "^[???-???A-z0-9]{1,10}$";   //??????,??????,?????? ?????? 10??? ????????????
                    Matcher matcher = Pattern.compile(nicPattern).matcher(s);
                    if (matcher.matches()) {
                        binding.eaTxvNameAlt.setText("???????????? ????????? ???????????????");
                        binding.eaTxvNameAlt.setTextColor(Color.parseColor("#E53A40"));
                        nameState = false;
                    } else {
                        binding.eaTxvNameAlt.setText("??????,??????,????????? ?????????????????????.");
                        binding.eaTxvNameAlt.setTextColor(Color.parseColor("#E53A40"));
                        nameState = false;
                    }
                }
            }
        });
        //????????? ??????
        binding.eaBtnNamecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nicPattern = "^[???-???A-z0-9]{1,10}$";   //
                Matcher matcher = Pattern.compile(nicPattern).matcher(binding.eaEdtName.getText());
                if (!binding.eaEdtName.getText().toString().isEmpty()&&matcher.matches()) {
                    String url = url_ + "/userapp/check_nick";
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    /* Create request */
                    StringRequest niccheckForm = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    boolean success = false;
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        success = jsonObject.getBoolean("result");
                                        if (success) {
                                            binding.eaTxvNameAlt.setText("??????????????? ??????????????????.");
                                            binding.eaTxvNameAlt.setTextColor(Color.parseColor("#5CAB7D"));
                                            binding.eaBtnNamecheck.setEnabled(false);
                                            nameState = true;
                                        } else {
                                            binding.eaTxvNameAlt.setText("?????? ???????????? ??????????????????");
                                            binding.eaTxvNameAlt.setTextColor(Color.parseColor("#E53A40"));
                                            nameState = false;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "??????????????? ????????? ????????????.", Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "?????????????????????.\n???????????? ?????? ??????????????????.", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    {
                        @Override
                        public  Map<String, String> getParams() throws AuthFailureError
                        {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("user_name", binding.eaEdtName.getText().toString());
                            return params;
                        }
                    };
                    niccheckForm.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(niccheckForm);
                } else {
                    //binding.nicnameAlt.setText("???????????? ???????????? ??????????????????.");
                    //binding.nicnameAlt.setTextColor(Color.parseColor("#E53A40"));
                    nameState = false;
                }
            }
        });
        //??????????????? ?????????
        binding.eaEdtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(holder.phonenum)){
                    binding.eaBtnCertsend.setEnabled(false);
                    phonenumState=false;
                }else {
                    binding.eaBtnCertsend.setEnabled(true);
                    String phonePattern = "^01[016789]{1}-?[0-9]{3,4}-?[0-9]{4}$";
                    Matcher matcher = Pattern.compile(phonePattern).matcher(s);
                    if(matcher.matches()){
                        binding.eaTxvPhoneAlt.setText("??????????????? ???????????????.");
                        binding.eaTxvPhoneAlt.setTextColor(Color.parseColor("#5CAB7D"));
                        phonenumState=true;
                    }else{
                        binding.eaTxvPhoneAlt.setText("????????? ?????? ?????? ???????????????.");
                        binding.eaTxvPhoneAlt.setTextColor(Color.parseColor("#E53A40"));
                        phonenumState=false;
                    }
                }
            }
        });
        //???????????? ???????????? ??? ???????????? ??????
        binding.eaBtnCertsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = url_ + "/userapp/check_phone";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                /* Create request */
                StringRequest pcheckForm = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                boolean success= false;
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    success = jsonObject.getBoolean("result");
                                    if (success){
                                        String sendurl = url_ + "/ajax/sms";
                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        /* Create request */
                                        StringRequest sendnumForm = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        //String pnPattern = "^01[016789]{1}-?[0-9]{3,4}-?[0-9]{4}$";
                                                        boolean success = false;
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            success = jsonObject.getBoolean("result");
                                                            if (success) {
                                                                binding.eaTxvPhoneAlt.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(getApplicationContext(), "??????????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                                                binding.eaBtnCertsend.setEnabled(false);
                                                                countDownTimer();
                                                                countDownTimer.start();
                                                                binding.eaEdtCerNum.setEnabled(true);
                                                                binding.eaBtnCheck.setEnabled(true);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                    Toast.makeText(getApplicationContext(), "??????????????? ????????? ????????????.", Toast.LENGTH_LONG).show();
                                                } else if (error instanceof AuthFailureError) {
                                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                } else if (error instanceof ServerError) {
                                                    Toast.makeText(getApplicationContext(), "?????????????????????.\n???????????? ?????? ??????????????????.", Toast.LENGTH_LONG).show();
                                                } else if (error instanceof NetworkError) {
                                                    Toast.makeText(getApplicationContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
                                                } else if (error instanceof ParseError) {
                                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        })
                                        {
                                            @Override
                                            public  Map<String, String> getParams() throws AuthFailureError
                                            {
                                                Map<String, String> params = new HashMap<String, String>();

                                                params.put("phone", binding.eaEdtPhone.getText().toString());
                                                cert_number = numberGen(6, 1);
                                                params.put("number", cert_number);
                                                return params;
                                            }
                                        };
                                        sendnumForm.setRetryPolicy(new DefaultRetryPolicy(
                                                0,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        queue.add(sendnumForm);

                                    }else {
                                        Toast.makeText(getApplicationContext(),"?????? ?????? ???????????????.\n?????? ????????? ??????????????????.",Toast.LENGTH_LONG).show();
                                        //binding.phoneAlt.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"??????????????? ????????? ????????????.",Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"?????????????????????.\n???????????? ?????? ??????????????????.",Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????.",Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                })
                {
                    @Override
                    public  Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("user_phone", binding.eaEdtPhone.getText().toString());
                        return params;
                    }
                };
                pcheckForm.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(pcheckForm);
            }
        });
        //???????????? ??????
        binding.eaBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cert_number.equals(binding.eaEdtCerNum.getText().toString())) {
                    binding.eaTxvCernumAlt.setText("?????????????????????.");
                    Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_LONG).show();
                    binding.eaEdtCerNum.setEnabled(false);
                    binding.eaEdtPhone.setEnabled(false);
                    binding.eaBtnCertsend.setText("????????????");
                    binding.eaBtnCertsend.setEnabled(false);
                    binding.eaBtnCheck.setEnabled(false);

                    countDownTimer.cancel();
                    phonecheckState = true;
                } else {
                    binding.eaTxvCernumAlt.setText("????????? ??????????????? ???????????????.");
                    phonecheckState = false;
                }
            }
        });
        //???????????? ?????????
        binding.eaBtnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwState||pwconState||nameState||phonecheckState){
                    String url = url_ + "/userapp/mypage_update";
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest editInfoForm = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    boolean success= false;
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        success = jsonObject.getBoolean("result");
                                        if (success){
                                            Toast.makeText(getApplicationContext(),"?????????????????????.",Toast.LENGTH_LONG).show();
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"?????? ??????.\n????????? ?????? ?????????????????????.",Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????."+error.getMessage(),Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(),"?????????????????????.\n???????????? ?????? ??????????????????.",Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????.",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    {
                        @Override
                        public  Map<String, String> getParams() throws AuthFailureError
                        {
                            Map<String, String> e_params = new HashMap<String, String>();
                            e_params.put("user_id", SaveSharedPreference.getUserIdx(getApplicationContext()).toString()); //???
                            if (pwconState){
                                e_params.put("user_pw", binding.eaEdtConPW.getText().toString());
                            }else{
                            }

                            if (nameState){
                                e_params.put("user_name", binding.eaEdtName.getText().toString());
                            }else {
                            }

                            if (phonecheckState) {
                                e_params.put("user_phone", binding.eaEdtPhone.getText().toString());
                            }else {
                            }
                            return e_params;
                        }
                    };
                    editInfoForm.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(editInfoForm);
                }else {
                    Toast.makeText(getApplicationContext(),"????????? ????????? ?????? ??????????????????.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //????????? ?????? ??????????????? ?????????
    public void countDownTimer() {
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                binding.eaBtnCertsend.setText((count / 60) + ":" + String.format("%02d", count % 60));//???:?????? ???????????? ?????????
                count--;
            }

            public void onFinish() {
                count = 180;
                cert_number = numberGen(6, 1);// ??????????????? ??????????????? ???????????? ????????? ????????????
                binding.eaBtnCertsend.setText("?????????");
                binding.eaBtnCertsend.setEnabled(true);// ???????????? ?????????
                binding.eaEdtCerNum.setEnabled(false);// ?????????????????? ????????????
                binding.eaBtnCheck.setEnabled(false);// ?????????????????? ????????????
            }
        };
    }
    @Override
    public void onBackPressed(){
        backPressed.backPressedAlert();
    }
    //???????????? ?????????
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
        }
        countDownTimer = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
            {
                // ?????? ????????? ????????? ??? ????????? ????????? ?????????.
                backPressed.backPressedAlert();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static String numberGen(int len, int dupCd) {

        Random rand = new Random();
        String numStr = ""; //????????? ????????? ??????

        for (int i = 0; i < len; i++) {

            //0~9 ?????? ?????? ??????
            String ran = Integer.toString(rand.nextInt(10));

            if (dupCd == 1) {
                //?????? ????????? numStr??? append
                numStr += ran;
            } else if (dupCd == 2) {
                //????????? ???????????? ????????? ????????? ?????? ????????? ????????????
                if (!numStr.contains(ran)) {
                    //????????? ?????? ????????? numStr??? append
                    numStr += ran;
                } else {
                    //????????? ????????? ???????????? ????????? ?????? ????????????
                    i -= 1;
                }
            }
        }
        return numStr;
    }
}


class EditACDataHolder {
    public String name;
    public String phonenum;
}
