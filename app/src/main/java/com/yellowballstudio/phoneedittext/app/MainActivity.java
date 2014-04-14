package com.yellowballstudio.phoneedittext.app;

import com.yellowballstudio.phoneedittext.PhoneEditText;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private PhoneEditText mPhone;
    private Button mSend;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mPhone = (PhoneEditText) findViewById(R.id.et_phone);
        mPhone.setHint(R.string.hint);
        showKeyboard(true);

        mSend = (Button) findViewById(R.id.btn_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhone.getText().toString().isEmpty()
                        || mPhone.getText().toString().length() < 18) {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_incorrect_number),
                            Toast.LENGTH_SHORT).show();
                } else {
                    showKeyboard(false);
                    new SendPhoneTask().execute();
                }
            }
        });
    }

    private class SendPhoneTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage(getString(R.string.toast_send));
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, getString(R.string.toast_send_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void showKeyboard(boolean isShow) {
        if (isShow) {
            imm.showSoftInput(mPhone, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(mPhone.getWindowToken(), 0);
        }
    }

}
