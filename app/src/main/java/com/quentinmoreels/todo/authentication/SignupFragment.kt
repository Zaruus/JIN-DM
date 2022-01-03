package com.quentinmoreels.todo.authentication

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import com.quentinmoreels.todo.MainActivity
import com.quentinmoreels.todo.databinding.FragmentSignupBinding
import com.quentinmoreels.todo.network.UserInfo
import com.quentinmoreels.todo.userinfo.UserInfoViewModel

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonValidateSignUp.setOnClickListener {
            val firstNameText = binding.editTextFirstNameSignUp.text.toString()
            val lastNameText = binding.editTextLastNameSignUp.text.toString()
            val emailText = binding.editTextEmailSignUp.text.toString()
            val passwordText = binding.editTextPasswordSignUp.text.toString()
            val passwordConfirmText = binding.editTextPasswordConfirmSignUp.text.toString()

            if (firstNameText != "" && lastNameText != "" && passwordText != ""
                && emailText != "" && passwordConfirmText != "" && passwordText == passwordConfirmText) {
                val newSignUpForm = SignUpForm(firstNameText, lastNameText, emailText, passwordText, passwordConfirmText)
                userInfoViewModel.signUpUserInfo(newSignUpForm)
                val fetchedToken = userInfoViewModel.signUpResponse.value.token

                if (fetchedToken != "defaultToken") {
                    Log.d("DEBUG", userInfoViewModel.signUpResponse.value.token)
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
                    }
                    userInfoViewModel.editUserInfo(UserInfo(emailText, firstNameText, lastNameText))

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "La creation du compte n'a pas fonctionne", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}