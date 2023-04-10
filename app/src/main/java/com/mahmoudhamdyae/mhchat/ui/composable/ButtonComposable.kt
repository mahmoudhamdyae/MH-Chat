package com.mahmoudhamdyae.mhchat.ui.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahmoudhamdyae.mhchat.R

@Composable
fun BasicTextButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    action: () -> Unit,
) {
    TextButton(onClick = action, modifier = modifier) { Text(text = stringResource(text)) }
}

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier = Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
    ) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}

@Composable
fun ButtonWithIcon(
    @StringRes text: Int,
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Button(onClick = action,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(id = text),
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun DialogConfirmButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun DialogCancelButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
    ) {
        Text(text = stringResource(text))
    }
}

@Preview
@Composable
fun DialogCancelButtonPreview() {
    DialogCancelButton(text = R.string.cancel) {}
}

@Preview
@Composable
fun DialogConfirmButtonPreview() {
    DialogConfirmButton(text = R.string.sign_out_confirm) {}
}