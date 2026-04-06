package com.example.beerwager.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.beerwager.ui.components.login.LoginRegisterView
import com.example.beerwager.ui.theme.AppTheme
import com.example.beerwager.ui.view_model.LoginRegisterViewModel
import com.example.beerwager.utils.TokenManager
import com.example.beerwager.utils.collectOnStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (tokenManager.getToken() != null) {
            navigateToMainActivity()
            return
        }
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize(),
                ) { padding ->

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        val screenHeight = maxHeight

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .heightIn(min = screenHeight),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            val viewModel = hiltViewModel<LoginRegisterViewModel>()
                            val state by viewModel.loginRegisterState.collectAsStateWithLifecycle()
                            viewModel.navigationChannel.collectOnStarted {
                                navigateToMainActivity()
                            }

                            viewModel.errorFlow.collectOnStarted {
                                Toast.makeText(this@LoginActivity, it.messageId, Toast.LENGTH_SHORT).show()
                            }

                            LoginRegisterView(state, viewModel::onEvent)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        MainActivity.start(this)
        finish()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}