package br.com.ambeco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ambeco.beans.UsuarioBean;
import br.com.ambeco.dao.LocalDAO;
import br.com.ambeco.helpers.UserHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText userText;
    private EditText passwordText;
    private Button btnLogin;
    private LocalDAO localDAO;
    private UserHelper userHelper = new UserHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LinearLayout boxLogin = (LinearLayout) findViewById(R.id.login_boxLogin);
        boxLogin.setVisibility(View.GONE);

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

                            UsuarioBean userCache = userHelper.getUserCache(LoginActivity.this);

                            UsuarioBean usuario = null;

                            if(userCache.getIdUsuario() != 0) {
                                usuario = localDAO.getUsuario(userCache.getEmail(), userCache.getSenha());
                            }

                            if(usuario != null) {
                                Intent intentLista = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intentLista);
                                finish();
                            } else {

                                Animation animTranslate = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate);
                                animTranslate.setAnimationListener(new Animation.AnimationListener() {

                                    @Override
                                    public void onAnimationStart(Animation arg0) {
                                        findViewById(R.id.login_progressBar).setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation arg0) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation arg0) {
                                        boxLogin.setVisibility(View.VISIBLE);
                                        Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                                        boxLogin.startAnimation(animFade);
                                    }
                                });
                                LinearLayout boxLogo = (LinearLayout) findViewById(R.id.login_boxLogo);
                                boxLogo.startAnimation(animTranslate);
                            }
                        }
                    });
                }
            }
        };
        timerThread.start();

        userText = (EditText) findViewById(R.id.login_user);
        userText.addTextChangedListener(textWatcher);

        passwordText = (EditText) findViewById(R.id.login_password);
        passwordText.addTextChangedListener(textWatcher);


        btnLogin = (Button) findViewById(R.id.login_btn_entrar);

        localDAO = new LocalDAO(this);

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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean validaCampos = validate(new EditText[]{userText, passwordText});
            if(validaCampos) {
                btnLogin.setEnabled(Boolean.TRUE);
            } else {
                btnLogin.setEnabled(Boolean.FALSE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void btnLoginClick(View view) {

        String usuario = userText.getText().toString();
        String senha = passwordText.getText().toString();

        UsuarioBean userBean = localDAO.getUsuario(usuario, senha);

        if(userBean == null) {
            Toast.makeText(this, "Usuário/Senha inválidos.", Toast.LENGTH_SHORT).show();
        } else {

            userHelper.setUserCache(this, userBean);

            Intent intentLista = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentLista);
            finish();
        }
    }

}
