package br.com.ambeco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);
        LoginBox.setVisibility(View.GONE);

        Thread timerThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Animation animTranslate  = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate);
                            animTranslate.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation arg0) { }

                                @Override
                                public void onAnimationRepeat(Animation arg0) { }

                                @Override
                                public void onAnimationEnd(Animation arg0) {
                                    LoginBox.setVisibility(View.VISIBLE);
                                    Animation animFade  = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                                    LoginBox.startAnimation(animFade);
                                }
                            });
                            ImageView imgLogo = (ImageView) findViewById(R.id.imageView1);
                            imgLogo.startAnimation(animTranslate);
                        }
                    });
                }
            }
        };
        timerThread.start();

        Button btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLista = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentLista);
            }
        });

        TextView cadastrarLoginText = (TextView) findViewById(R.id.login_sign);
        cadastrarLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intentRegistration);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        EditText userText = (EditText) findViewById(R.id.login_user);
        EditText passwordText = (EditText) findViewById(R.id.login_password);
        Button btnLogin = (Button) findViewById(R.id.login_btn_login);

        boolean validaCampos = validate(new EditText[]{userText, passwordText});
        if(validaCampos) {
            btnLogin.setEnabled(Boolean.TRUE);
        } else {
            btnLogin.setEnabled(Boolean.FALSE);
        }
        return super.onKeyUp(keyCode, event);
    }
}
