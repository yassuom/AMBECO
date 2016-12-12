package br.com.ambeco;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.VectorEnabledTintResources;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import br.com.ambeco.beans.UsuarioBean;
import br.com.ambeco.dao.LocalDAO;

public class RegistrationActivity extends AppCompatActivity {

    private LinearLayout registrationBox1;
    private LinearLayout registrationBox2;
    private LinearLayout registrationBox3;
    private LinearLayout registrationBox4;
    private Button btnContinue;
    private EditText edtEmail;
    private EditText edtNome;
    private EditText edtSobrenome;
    private EditText edtSenha;
    private EditText edtConfirmarSenha;
    private LocalDAO localDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationBox1 = (LinearLayout) findViewById(R.id.registration_box1);
        registrationBox2 = (LinearLayout) findViewById(R.id.registration_box2);
        registrationBox3 = (LinearLayout) findViewById(R.id.registration_box3);
        registrationBox4 = (LinearLayout) findViewById(R.id.registration_box4);

        btnContinue = (Button) findViewById(R.id.registration_btnContinue);

        edtEmail = (EditText) findViewById(R.id.registration_edtEmail);
        edtNome = (EditText) findViewById(R.id.registration_edtNome);
        edtSobrenome = (EditText) findViewById(R.id.registration_edtSobrenome);
        edtSenha = (EditText) findViewById(R.id.registration_edtSenha);
        edtConfirmarSenha = (EditText) findViewById(R.id.registration_edtConfirmarSenha);

        localDao = new LocalDAO(this);
    }

    public void onContinueClick(View view) {

        if (registrationBox1.isShown()) {
            changeRegistrationBox(registrationBox1, registrationBox2, Boolean.FALSE);
        } else if (registrationBox2.isShown()) {
            if(edtEmail.length() > 0) {
                if(localDao.usuarioExistente(edtEmail.getText().toString())) {
                    Toast.makeText(this, "Usuário informado já possui cadastro.", Toast.LENGTH_SHORT).show();
                } else {
                    changeRegistrationBox(registrationBox2, registrationBox3, Boolean.FALSE);
                }
            } else {
                Toast.makeText(this, "Campo e-mail obrigatório.", Toast.LENGTH_SHORT).show();
                edtEmail.setFocusable(Boolean.TRUE);
            }
        } else if (registrationBox3.isShown()) {
            if((edtNome.length() > 0)&&(edtSobrenome.length() > 0)) {
                changeRegistrationBox(registrationBox3, registrationBox4, Boolean.FALSE);
            } else {
                Toast.makeText(this, "Campos nome/sobrenome obrigatórios.", Toast.LENGTH_SHORT).show();
                edtNome.setFocusable(Boolean.TRUE);
            }
        } else if (registrationBox4.isShown()) {
            if((edtSenha.length() > 0)&&(edtConfirmarSenha.length() > 0)) {
                if(edtSenha.getText().length() < 6) {
                    Toast.makeText(this, "Campo senha deve conter pelo menos seis caracteres.", Toast.LENGTH_SHORT).show();
                    edtSenha.setFocusable(Boolean.TRUE);
                } else if(!edtSenha.getText().toString().equalsIgnoreCase(edtConfirmarSenha.getText().toString())){
                    Toast.makeText(this, "As senhas informadas não conferem.", Toast.LENGTH_SHORT).show();
                    edtSenha.setFocusable(Boolean.TRUE);
                } else {
                    insereUsuario();
                }
            } else {
                Toast.makeText(this, "Campo senha/confirmar senha obrigatórios.", Toast.LENGTH_SHORT).show();
                edtSenha.setFocusable(Boolean.TRUE);
            }
        }
    }

    private void insereUsuario() {
        UsuarioBean userBean = new UsuarioBean();
        String strEmail = edtEmail.getText().toString();
        String strNome = edtNome.getText().toString();
        String strSobrenome = edtSobrenome.getText().toString();
        String strSenha = edtSenha.getText().toString();

        userBean.setEmail(strEmail);
        userBean.setNome(strNome);
        userBean.setSobrenome(strSobrenome);
        userBean.setSenha(strSenha);

        localDao.insertUsuario(userBean);

        Toast.makeText(this, "Cadastro concluído com sucesso.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void changeRegistrationBox(final LinearLayout boxFrom, final LinearLayout boxTo, final boolean ret) {

        final LinearLayout mainContent = (LinearLayout) findViewById(R.id.activity_registration);

        Animation animation = null;

        if(ret) {
            animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.right_left_animation);
        } else {
            animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.left_right_animation);
        }

        boxFrom.setVisibility(View.INVISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(!ret) {
                    boxTo.setVisibility(View.VISIBLE);
                }
                mainContent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(ret) {
                    boxTo.setVisibility(View.VISIBLE);
                }
                mainContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mainContent.startAnimation(animation);
    }

    public void onTxtEntrarClick(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int witch) {
                if(witch == DialogInterface.BUTTON_POSITIVE) {
                    finish();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Tem certeza de que deseja cancelar a criação da conta? Isso fará com que todas as informações inseridas por você até agora sejam descartadas.")
                .setPositiveButton("Sim",dialogClickListener).
                setNegativeButton("Não", dialogClickListener).show();
    }

    @Override
    public void onBackPressed() {
        if (registrationBox1.isShown()) {
            finish();
        } else if (registrationBox2.isShown()) {
            changeRegistrationBox(registrationBox2, registrationBox1, Boolean.TRUE);
        } else if (registrationBox3.isShown()) {
            changeRegistrationBox(registrationBox3, registrationBox2, Boolean.TRUE);
        } else if (registrationBox4.isShown()) {
            changeRegistrationBox(registrationBox4, registrationBox3, Boolean.TRUE);
        }
    }

}
