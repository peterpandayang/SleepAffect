package com.example.bingkunyang.teenplus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText firstnameEdittext,lastnameEdittext,emailEdittext,passEdittext,passAgainEdittext,birthdayEdittext;
    private RadioGroup genderRadioGroup;
    private Button registerButton, signInButton, resetPasswordButton;
    private DatePickerDialog datePickerDialog;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind views with their ids
        bindViews();

        //Set listeners of views
        setViewActions();

        //Create DatePickerDialog to show a calendar to user to select birthdate
        prepareDatePickerDialog();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

    }

    private void bindViews() {
        firstnameEdittext=(EditText)findViewById(R.id.firstname_edittext);
        lastnameEdittext=(EditText)findViewById(R.id.lastname_edittext);
        emailEdittext=(EditText)findViewById(R.id.email_edittext);
        passEdittext=(EditText)findViewById(R.id.password_edittext);
        passAgainEdittext=(EditText)findViewById(R.id.password_again_edittext);
        birthdayEdittext=(EditText)findViewById(R.id.birthday_edittext);
        genderRadioGroup=(RadioGroup)findViewById(R.id.gender_radiogroup);
        registerButton=(Button)findViewById(R.id.register_button);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resetPasswordButton = (Button) findViewById(R.id.btn_reset_password);
    }

    private void setViewActions() {
        birthdayEdittext.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.birthday_edittext:
                //Show a Calendar
                datePickerDialog.show();
                break;
            case R.id.register_button:
                //show toast with typed and selected values
                showToastWithFormValues();
                break;
            case R.id.btn_reset_password:
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.sign_in_button:
//                finish();
                Intent i=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }
    }

    private void prepareDatePickerDialog() {
        //Get current date
        Calendar calendar=Calendar.getInstance();

        //Create datePickerDialog with initial date which is current and decide what happens when a date is selected.
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //When a date is selected, it comes here.
                //Change birthdayEdittext's text and dismiss dialog.
                birthdayEdittext.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                datePickerDialog.dismiss();
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void showToastWithFormValues() {

        //Get edittexts values
        String firstname=firstnameEdittext.getText().toString();
        String lastname=lastnameEdittext.getText().toString();
        String email=emailEdittext.getText().toString();
        String pass=passEdittext.getText().toString();
        String passAgain=passAgainEdittext.getText().toString();
        String birthday=birthdayEdittext.getText().toString();

        //Get gender
        RadioButton selectedRadioButton=(RadioButton)findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String gender=selectedRadioButton==null ? "":selectedRadioButton.getText().toString();

        //Check all fields
        if(!firstname.equals("")&&!lastname.equals("")&&!email.equals("")&&!pass.equals("")&&!passAgain.equals("")&&!birthday.equals("")&&!gender.equals("")){

            //Check if pass and passAgain are the same
            if(pass.equals(passAgain)){

                //Everything allright
                Toast.makeText(this,getResources().getString(R.string.here_is_values,("\nFirstname:"+firstname+"\nLastname:"+lastname+"\nEmail:"+email+"\nBirthday:"+birthday+"\nGender:"+gender)),Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,getResources().getString(R.string.passwords_must_be_the_same),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.no_field_can_be_empty),Toast.LENGTH_SHORT).show();
        }

        if (pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // should make request here
        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}
