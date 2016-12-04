package br.com.ambeco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RegistrationActivity extends AppCompatActivity {

    private LinearLayout registrationBox1;
    private LinearLayout registrationBox2;
    private LinearLayout registrationBox3;
    private LinearLayout registrationBox4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationBox1 = (LinearLayout) findViewById(R.id.registration_box1);
        registrationBox2 = (LinearLayout) findViewById(R.id.registration_box2);
        registrationBox3 = (LinearLayout) findViewById(R.id.registration_box3);
        registrationBox4 = (LinearLayout) findViewById(R.id.registration_box4);
    }

    public void onContinueClick(View view) {
        changeRegistrationBox(registrationBox1,registrationBox2);
    }

    private void changeRegistrationBox(final LinearLayout boxFrom, final LinearLayout boxTo) {

        final LinearLayout mainContent = (LinearLayout) findViewById(R.id.activity_registration);

        Animation leftToRight = AnimationUtils.loadAnimation(getBaseContext(), R.anim.left_right_animation);

        boxFrom.setVisibility(View.INVISIBLE);
        leftToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                boxTo.setVisibility(View.VISIBLE);
                mainContent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mainContent.startAnimation(leftToRight);
    }

}
