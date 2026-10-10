plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    implementation(project(":shared"))
}

application {
    mainClass.set("br.edu.utfpr.consultacepkmp.MainKt")
}