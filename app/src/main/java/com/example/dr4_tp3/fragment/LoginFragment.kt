package com.example.dr4_tp3.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.dr4_tp3.R
import com.example.dr4_tp3.viewModel.UsuarioViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var usuarioViewModel: UsuarioViewModel
    private var callbackManager = CallbackManager.Factory.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            usuarioViewModel = ViewModelProviders.of(it).get(UsuarioViewModel::class.java)
        }
        txtSemCadastro.setOnClickListener{
            findNavController().navigate(R.id.cadastroFragment)
        }
        btnLogin.setOnClickListener {
            if(txtEmail.text.isNullOrBlank()|| txtSenha.text.isNullOrBlank()) {
                Toast.makeText(context, "Preencha todas as informações", Toast.LENGTH_SHORT).show()
            }else{
                usuarioViewModel.loginFirestore(requireContext().applicationContext,txtEmail.text.toString(), txtSenha.text.toString())
                }
            }
        btnLoginFB.setReadPermissions("email", "public_profile")
        btnLoginFB.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                    // Codigo completo
                }
                override fun onCancel() {
                    //
                }
                override fun onError(error: FacebookException) {
                    Toast.makeText(
                        this@LoginFragment.requireContext(),
                        error.message, Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
        private fun handleFacebookAccessToken(token: AccessToken) {
            val credential = FacebookAuthProvider.getCredential(token.token)
            val auth = FirebaseAuth.getInstance()
            val task = auth.signInWithCredential(credential)

            task.addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Alterando a interface para a Home
                } else {
                    // Mensagem de erro.
                }
            }
        }
    }
