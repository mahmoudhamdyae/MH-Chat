package com.mahmoudhamdyae.mhchat.common.ext

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
    return this.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 0.dp)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
}

fun Modifier.card(): Modifier {
    return this.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 8.dp)
}

fun Modifier.contextMenu(): Modifier {
    return this.wrapContentWidth()
}

fun Modifier.dropdownSelector(): Modifier {
    return this.fillMaxWidth()
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
    return this.wrapContentSize(align = Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
    return this.fillMaxWidth().padding(all = 12.dp)
}

fun Modifier.smallSpacer(): Modifier {
    return this.fillMaxWidth().height(height = 8.dp)
}