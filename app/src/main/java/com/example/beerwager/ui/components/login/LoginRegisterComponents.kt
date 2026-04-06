package com.example.beerwager.ui.components.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.beerwager.R
import com.example.beerwager.utils.Dimen

@Composable
fun LoginTitle(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(
            text = stringResource(R.string.login_are_you_ready_for_a),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.size(Dimen.SPACING_XXS))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.SportsBar,
                contentDescription = null,
                modifier = Modifier.size(Dimen.ICON_SIZE_BIG)
            )
            Text(
                text = stringResource(R.string.login_wager),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun LoginTextField(
    label: String,
    isPassword: Boolean,
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (error != null) MaterialTheme.colorScheme.error else Color.Unspecified
        )
        Spacer(modifier = Modifier.padding(Dimen.SPACING_XXS))
        OutlinedTextField(
            value = value,
            isError = error != null,
            supportingText = {
                if (error != null) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            onValueChange = onValueChanged,
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(
                        imageVector = image,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = Dimen.SPACING_XXS)
                            .clickable { passwordVisible = !passwordVisible }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}