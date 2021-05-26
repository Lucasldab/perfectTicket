package com.example.perfectticket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.perfectticket.MainActivity;
import com.example.perfectticket.Model.UserModel;
import com.example.perfectticket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import org.jetbrains.annotations.NotNull;

public class Cadastrar extends AppCompatActivity {

    private EditText edit_nome_cadastrar;
    private EditText edit_sobrenome_cadastrar;
    private EditText edit_email_cadastrar;
    private EditText edit_senha_cadastrar;
    private EditText edit_confirmar_senha_cadastrar;
    private CheckBox ckb_mostrar_senha_cadastrar;
    private Button button_cadastrar_cadastrar;
    private Button button_login_cadastrar;
    private ProgressBar cadastrar_barra_progresso;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        mAuth = FirebaseAuth.getInstance();

        edit_nome_cadastrar = findViewById(R.id.edit_nome_cadastrar);
        edit_sobrenome_cadastrar = findViewById(R.id.edit_sobrenome_cadastrar);
        edit_email_cadastrar = findViewById(R.id.edit_email_cadastrar);
        edit_senha_cadastrar = findViewById(R.id.edit_senha_cadastrar);
        edit_confirmar_senha_cadastrar = findViewById(R.id.edit_confirmar_senha_cadastrar);
        ckb_mostrar_senha_cadastrar = findViewById(R.id.ckb_mostrar_senha_cadastrar);
        button_cadastrar_cadastrar = findViewById(R.id.button_cadastrar_cadastrar);
        button_login_cadastrar = findViewById(R.id.button_login_cadastrar);
        cadastrar_barra_progresso = findViewById(R.id.cadastrar_barra_progresso);

        ckb_mostrar_senha_cadastrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_senha_cadastrar.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_confirmar_senha_cadastrar.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_senha_cadastrar.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_confirmar_senha_cadastrar.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        button_cadastrar_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserModel userModel = new UserModel();

                userModel.setEmail(edit_email_cadastrar.getText().toString());
                userModel.setNome(edit_nome_cadastrar.getText().toString());
                userModel.setSobrenome(edit_sobrenome_cadastrar.getText().toString());
                String senha = edit_senha_cadastrar.getText().toString();
                String confirmarSenha = edit_confirmar_senha_cadastrar.getText().toString();

                if (!TextUtils.isEmpty(userModel.getNome()) && !TextUtils.isEmpty(userModel.getSobrenome()) && !TextUtils.isEmpty(userModel.getEmail()) && !TextUtils.isEmpty(senha) && !TextUtils.isEmpty(confirmarSenha)){
                    if (senha.equals(confirmarSenha)){
                        cadastrar_barra_progresso.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(userModel.getEmail(),senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    userModel.setId(mAuth.getUid());
                                    userModel.salvar();
                                    abrirTelaPrincipal();
                                }else {
                                    String error;
                                    try {
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                            error = "A senha deve conter no mínimo 6 caracteres";
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        error = "E-mail inválido";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        error = "E-mail já cadastrado";
                                    }catch (Exception e){
                                        error = "Erro ao efetuar o cadastro";
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(Cadastrar.this, error, Toast.LENGTH_SHORT).show();
                                }
                                cadastrar_barra_progresso.setVisibility(View.INVISIBLE);
                            }
                        });
                    }else {
                        Toast.makeText(Cadastrar.this,"A senha deve ser a mesma em ambos os campos!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Cadastrar.this, "Preencha todos os campos!" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_login_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastrar.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Cadastrar.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
