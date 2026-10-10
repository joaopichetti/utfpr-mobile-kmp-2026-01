package br.edu.utfpr.consultacepkmp

import br.edu.utfpr.shared.platform
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val frame = JFrame("Consulta CEP KMP - Desktop").apply {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            preferredSize = Dimension(600, 500)
            add(JLabel("Plataforma: ${platform()}", SwingConstants.CENTER))
            pack()
            setLocationRelativeTo(null)
            isVisible = true
        }
    }
}