package br.edu.utfpr.shared

import android.os.Build

actual fun platform() = "Android ${Build.VERSION.SDK_INT}"