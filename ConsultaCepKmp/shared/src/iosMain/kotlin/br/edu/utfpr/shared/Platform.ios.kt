package br.edu.utfpr.shared

import platform.UIKit.UIDevice

actual fun platform() = "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"