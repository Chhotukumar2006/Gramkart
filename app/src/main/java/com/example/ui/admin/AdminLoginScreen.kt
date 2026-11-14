package com.example.ui.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.GramKartViewModel
import com.example.ui.components.LanguageTogglePill
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.GramBackground
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramError
import com.example.ui.theme.GramErrorContainer
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramOrangeAccent
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite

@Composable
fun AdminLoginScreen(
    language: AppLanguage,
    strings: AppStrings,
    onToggleLanguage: () -> Unit,
    onBackToStore: () -> Unit,
    onLoginAttempt: (email: String, password: String) -> Boolean
) {
    BackHandler(onBack = onBackToStore)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GramBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onBackToStore,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, GramBorder),
                modifier = Modifier.testTag("admin_login_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(strings.backToStore)
            }
            LanguageTogglePill(language = language, onToggle = onToggleLanguage)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GramWhite),
                border = BorderStroke(1.dp, GramBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.widthIn(max = 440.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = GramGreenContainer,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null,
                                tint = GramGreenDark,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Text(
                        text = strings.adminLoginTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        color = GramGreenDark,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = strings.adminLoginSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GramTextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GramOrangeContainer.copy(alpha = 0.75f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = strings.demoCredsHint,
                                style = MaterialTheme.typography.labelSmall,
                                color = GramOrangeDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                onClick = {
                                    email = GramKartViewModel.DEMO_ADMIN_EMAIL
                                    password = GramKartViewModel.DEMO_ADMIN_PASSWORD
                                    errorMessage = null
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = GramOrangeAccent,
                                modifier = Modifier.testTag("fill_demo_creds_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Key,
                                        contentDescription = null,
                                        tint = GramWhite,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = strings.fillDemoCreds,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GramWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        label = { Text(strings.emailLabel) },
                        leadingIcon = {
                            Icon(Icons.Rounded.Email, contentDescription = null, tint = GramGreenPrimary)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_email_input")
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text(strings.passwordLabel) },
                        leadingIcon = {
                            Icon(Icons.Rounded.Lock, contentDescription = null, tint = GramGreenPrimary)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input")
                    )

                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = GramErrorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = GramError,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val ok = onLoginAttempt(email, password)
                            if (!ok) {
                                errorMessage = strings.invalidCredentials
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("admin_login_submit_button")
                    ) {
                        Text(strings.loginBtn, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
