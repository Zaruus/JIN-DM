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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.quentinmoreels.todo.MainActivity
import com.quentinmoreels.todo.R
import com.quentinmoreels.todo.databinding.FragmentLoginBinding
import com.quentinmoreels.todo.userinfo.UserInfoViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonValidateLogIn.setOnClickListener {
            val emailText = binding.editTextEmailLogIn.text.toString()
            val passwordText = binding.editTextPassword.text.toString()

            if (emailText != "" && passwordText != "") {
                val newLoginForm = LoginForm(emailText, passwordText)
                userInfoViewModel.loginUserInfo(newLoginForm)
                val fetchedToken = userInfoViewModel.loginResponse.value.token

                if (fetchedToken != "defaultToken") {
                    Log.d("DEBUG", fetchedToken)
                    val defaultSharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context)
                    defaultSharedPreferences.edit {
                        putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
                    }
                    Log.d("DEBUG", defaultSharedPreferences.getString(SHARED_PREF_TOKEN_KEY, "").toString())
                    findNavController().navigate(R.id.action_LoginFragment_to_taskListFragment)
                } else {
                    Toast.makeText(context, "Le login n'a pas fonctionné", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}