package br.edu.utfpr.consultacepkmp

import br.edu.utfpr.consultacepkmp.ui.CepUiState
import br.edu.utfpr.consultacepkmp.ui.CepViewModel
import br.edu.utfpr.shared.platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun main() {
    SwingUtilities.invokeLater {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (_: Exception) {}

        val viewModel = CepViewModel()
        createAndShowGui(viewModel)
    }
}

private fun createAndShowGui(viewModel: CepViewModel) {
    val frame = JFrame("Consulta CEP KMP - Desktop").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        minimumSize = Dimension(480, 400)
        setSize(500, 420)
        setLocationRelativeTo(null)
    }

    val mainPanel = JPanel(BorderLayout(0, 16)).apply {
        border = BorderFactory.createEmptyBorder(20, 24, 20, 24)
    }

    // Header com Título e Plataforma
    val headerPanel = JPanel(BorderLayout(0, 4)).apply {
        val titleLabel = JLabel("Consulta de CEP").apply {
            font = font.deriveFont(Font.BOLD, 20f)
        }
        val platformLabel = JLabel("Plataforma: ${platform()}").apply {
            font = font.deriveFont(Font.PLAIN, 11f)
            foreground = Color.GRAY
        }
        add(titleLabel, BorderLayout.NORTH)
        add(platformLabel, BorderLayout.SOUTH)
    }

    // Formulário de busca
    val searchPanel = JPanel(GridBagLayout())
    val gbcSearch = GridBagConstraints().apply {
        insets = Insets(4, 4, 4, 4)
        anchor = GridBagConstraints.WEST
    }

    val lblCep = JLabel("CEP:").apply {
        font = font.deriveFont(Font.BOLD, 13f)
    }
    val txtCep = JTextField(10).apply {
        font = font.deriveFont(Font.PLAIN, 14f)
        margin = Insets(4, 6, 4, 6)
    }
    val btnConsultar = JButton("Consultar CEP").apply {
        font = font.deriveFont(Font.BOLD, 13f)
    }

    gbcSearch.gridx = 0
    gbcSearch.gridy = 0
    gbcSearch.weightx = 0.0
    gbcSearch.fill = GridBagConstraints.NONE
    searchPanel.add(lblCep, gbcSearch)

    gbcSearch.gridx = 1
    gbcSearch.weightx = 1.0
    gbcSearch.fill = GridBagConstraints.HORIZONTAL
    searchPanel.add(txtCep, gbcSearch)

    gbcSearch.gridx = 2
    gbcSearch.weightx = 0.0
    gbcSearch.fill = GridBagConstraints.NONE
    searchPanel.add(btnConsultar, gbcSearch)

    val topContainer = JPanel(BorderLayout(0, 12)).apply {
        add(headerPanel, BorderLayout.NORTH)
        add(searchPanel, BorderLayout.SOUTH)
    }
    mainPanel.add(topContainer, BorderLayout.NORTH)

    // Painel de conteúdo com CardLayout para os 4 estados
    val cardLayout = CardLayout()
    val contentPanel = JPanel(cardLayout)

    val lblError = JLabel()
    val lblLogradouro = JLabel()
    val lblBairro = JLabel()
    val lblCidadeUf = JLabel()
    val lblCepResultado = JLabel()

    contentPanel.add(createIdlePanel(), "IDLE")
    contentPanel.add(createLoadingPanel(), "LOADING")
    contentPanel.add(createErrorPanel(lblError), "ERROR")
    contentPanel.add(
        createSuccessPanel(
            lblLogradouro = lblLogradouro,
            lblBairro = lblBairro,
            lblCidadeUf = lblCidadeUf,
            lblCep = lblCepResultado
        ),
        "SUCCESS"
    )

    mainPanel.add(contentPanel, BorderLayout.CENTER)
    frame.contentPane = mainPanel

    // Sincronização entre JTextField e CepViewModel
    var isUpdatingFromModel = false
    txtCep.document.addDocumentListener(object : DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) = onUpdate()
        override fun removeUpdate(e: DocumentEvent?) = onUpdate()
        override fun changedUpdate(e: DocumentEvent?) = onUpdate()

        private fun onUpdate() {
            if (!isUpdatingFromModel) {
                viewModel.onCepChange(txtCep.text)
            }
        }
    })

    btnConsultar.addActionListener {
        viewModel.buscarCep()
    }
    txtCep.addActionListener {
        viewModel.buscarCep()
    }

    // Coleta dos estados reativos do ViewModel
    val scope = CoroutineScope(Dispatchers.Main)
    scope.launch {
        viewModel.cepInput.collect { text ->
            if (txtCep.text != text) {
                isUpdatingFromModel = true
                txtCep.text = text
                isUpdatingFromModel = false
            }
        }
    }

    scope.launch {
        viewModel.uiState.collect { state ->
            btnConsultar.isEnabled = state !is CepUiState.Loading

            when (state) {
                is CepUiState.Idle -> {
                    cardLayout.show(contentPanel, "IDLE")
                }
                is CepUiState.Loading -> {
                    cardLayout.show(contentPanel, "LOADING")
                }
                is CepUiState.Error -> {
                    lblError.text = state.mensagem
                    cardLayout.show(contentPanel, "ERROR")
                }
                is CepUiState.Success -> {
                    val endereco = state.endereco
                    lblLogradouro.text = endereco.logradouro.ifBlank { "Não informado" }
                    lblBairro.text = endereco.bairro.ifBlank { "Não informado" }
                    lblCidadeUf.text = "${endereco.cidade} - ${endereco.uf}".trim('-', ' ')
                    lblCepResultado.text = formatarCep(endereco.cep)
                    cardLayout.show(contentPanel, "SUCCESS")
                }
            }
        }
    }

    frame.isVisible = true
}

private fun createIdlePanel(): JPanel {
    val panel = JPanel(GridBagLayout())
    val label = JLabel("Digite um CEP de 8 dígitos para consultar", SwingConstants.CENTER).apply {
        font = font.deriveFont(Font.PLAIN, 13f)
        foreground = Color.GRAY
    }
    panel.add(label)
    return panel
}

private fun createLoadingPanel(): JPanel {
    val panel = JPanel(GridBagLayout())
    val inner = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
    }
    val progressBar = JProgressBar().apply {
        isIndeterminate = true
        preferredSize = Dimension(200, 14)
        alignmentX = 0.5f
    }
    val label = JLabel("Consultando CEP...").apply {
        font = font.deriveFont(Font.PLAIN, 12f)
        foreground = Color.GRAY
        alignmentX = 0.5f
    }
    inner.add(progressBar)
    inner.add(Box.createVerticalStrut(10))
    inner.add(label)
    panel.add(inner)
    return panel
}

private fun createErrorPanel(lblErrorMessage: JLabel): JPanel {
    val panel = JPanel(BorderLayout(0, 8)).apply {
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color(0xEF, 0x44, 0x44), 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        )
        background = Color(0xFE, 0xF2, 0xF2)
    }

    val titleLabel = JLabel("Atenção").apply {
        font = font.deriveFont(Font.BOLD, 14f)
        foreground = Color(0xDC, 0x26, 0x26)
    }
    panel.add(titleLabel, BorderLayout.NORTH)

    lblErrorMessage.apply {
        font = font.deriveFont(Font.PLAIN, 13f)
        foreground = Color(0x99, 0x1B, 0x1B)
    }
    panel.add(lblErrorMessage, BorderLayout.CENTER)

    val wrapper = JPanel(BorderLayout()).apply {
        add(panel, BorderLayout.NORTH)
    }
    return wrapper
}

private fun createSuccessPanel(
    lblLogradouro: JLabel,
    lblBairro: JLabel,
    lblCidadeUf: JLabel,
    lblCep: JLabel
): JPanel {
    val panel = JPanel(BorderLayout(0, 12)).apply {
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color(0x3B, 0x82, 0xF6), 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        )
        background = Color(0xF8, 0xFA, 0xFC)
    }

    val titleLabel = JLabel("Endereço Encontrado").apply {
        font = font.deriveFont(Font.BOLD, 15f)
        foreground = Color(0x1E, 0x40, 0xAF)
    }
    panel.add(titleLabel, BorderLayout.NORTH)

    val gridPanel = JPanel(GridBagLayout()).apply {
        isOpaque = false
    }
    val gbc = GridBagConstraints().apply {
        insets = Insets(5, 4, 5, 12)
        anchor = GridBagConstraints.WEST
        fill = GridBagConstraints.HORIZONTAL
    }

    fun addRow(row: Int, label: String, valueLabel: JLabel) {
        gbc.gridx = 0
        gbc.gridy = row
        gbc.weightx = 0.0
        val l = JLabel(label).apply {
            font = font.deriveFont(Font.BOLD, 12f)
            foreground = Color(0x47, 0x55, 0x69)
        }
        gridPanel.add(l, gbc)

        gbc.gridx = 1
        gbc.weightx = 1.0
        valueLabel.font = valueLabel.font.deriveFont(Font.PLAIN, 13f)
        valueLabel.foreground = Color(0x0F, 0x17, 0x2A)
        gridPanel.add(valueLabel, gbc)
    }

    addRow(0, "Logradouro:", lblLogradouro)
    addRow(1, "Bairro:", lblBairro)
    addRow(2, "Cidade/UF:", lblCidadeUf)
    addRow(3, "CEP:", lblCep)

    panel.add(gridPanel, BorderLayout.CENTER)

    val wrapper = JPanel(BorderLayout()).apply {
        add(panel, BorderLayout.NORTH)
    }
    return wrapper
}

private fun formatarCep(cep: String): String {
    val digitsOnly = cep.filter { it.isDigit() }
    return if (digitsOnly.length == 8) {
        "${digitsOnly.substring(0, 5)}-${digitsOnly.substring(5)}"
    } else {
        cep
    }
}