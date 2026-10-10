//
// Created by João Guilherme Brasil Pichetti on 10/10/26.
//

import SwiftUI
import sharedKit

@MainActor
struct CepScreen: View {
    @StateObject private var viewModel: CepViewModel
    @FocusState private var isTextFieldFocused: Bool

    init() {
        _viewModel = StateObject(wrappedValue: CepViewModel())
    }

    init(viewModel: CepViewModel) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    // Cores inspiradas no tema Material 3 do app Android
    private let primaryColor = Color(red: 0.40, green: 0.31, blue: 0.64) // Purple40 (0xFF6650a4)
    private let primaryContainerColor = Color(red: 0.92, green: 0.87, blue: 1.0) // 0xFFEADDFF
    private let onPrimaryContainerColor = Color(red: 0.13, green: 0.0, blue: 0.36) // 0xFF21005D
    private let errorContainerColor = Color(red: 0.98, green: 0.87, blue: 0.86) // 0xFFF9DEDC
    private let onErrorContainerColor = Color(red: 0.25, green: 0.05, blue: 0.04) // 0xFF410E0B
    private let errorColor = Color(red: 0.70, green: 0.15, blue: 0.12) // 0xFFB3261E

    var body: some View {
        VStack(spacing: 0) {
            // TopAppBar: "Consulta de CEP"
            HStack {
                Text("Consulta de CEP")
                    .font(.title3)
                    .fontWeight(.semibold)
                    .foregroundColor(onPrimaryContainerColor)
                Spacer()
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)
            .background(primaryContainerColor.ignoresSafeArea(edges: .top))

            // Conteúdo principal
            ScrollView {
                VStack(spacing: 0) {
                    // Seção de busca (Topo)
                    VStack(alignment: .leading, spacing: 4) {
                        Text("CEP")
                            .font(.caption)
                            .foregroundColor(isTextFieldFocused ? primaryColor : .secondary)

                        HStack {
                            TextField("Ex: 01001000", text: Binding(
                                get: { viewModel.cepInput },
                                set: { viewModel.onCepChange($0) }
                            ))
                                .keyboardType(.numberPad)
                                .focused($isTextFieldFocused)
                                .submitLabel(.search)
                                .onSubmit {
                                    isTextFieldFocused = false
                                    viewModel.buscarCep()
                                }
                        }
                        .padding(.horizontal, 12)
                        .padding(.vertical, 12)
                        .background(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(isTextFieldFocused ? primaryColor : Color.gray.opacity(0.4), lineWidth: isTextFieldFocused ? 2 : 1)
                        )
                    }
                    .padding(.top, 16)

                    Spacer().frame(height: 12)

                    // Botão Consultar CEP
                    Button(action: {
                        isTextFieldFocused = false
                        viewModel.buscarCep()
                    }) {
                        Text("Consultar CEP")
                            .font(.body)
                            .fontWeight(.medium)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .frame(height: 48)
                            .background(viewModel.uiState == .loading ? Color.gray.opacity(0.5) : primaryColor)
                            .cornerRadius(24)
                    }
                    .disabled(viewModel.uiState == .loading)

                    Spacer().frame(height: 24)

                    // Seção de conteúdo reativo com transição suave
                    ZStack {
                        switch viewModel.uiState {
                        case .idle:
                            IdleStateContent(primaryColor: primaryColor)
                                .transition(.opacity)
                        case .loading:
                            LoadingStateContent()
                                .transition(.opacity)
                        case .error(let mensagem):
                            ErrorStateContent(
                                mensagem: mensagem,
                                containerColor: errorContainerColor,
                                textColor: onErrorContainerColor,
                                errorColor: errorColor
                            )
                                .transition(.opacity)
                        case .success(let endereco):
                            SuccessStateContent(
                                endereco: endereco,
                                primaryColor: primaryColor
                            )
                                .transition(.opacity)
                        }
                    }
                    .animation(.easeInOut(duration: 0.25), value: viewModel.uiState)
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 16)
            }
            .scrollDismissesKeyboard(.immediately)
        }
        .background(Color(UIColor.systemBackground).ignoresSafeArea())
        .toolbar {
            ToolbarItemGroup(placement: .keyboard) {
                Spacer()
                Button("Buscar") {
                    isTextFieldFocused = false
                    viewModel.buscarCep()
                }
            }
        }
    }
}

// MARK: - Idle State Content
private struct IdleStateContent: View {
    let primaryColor: Color

    var body: some View {
        VStack(spacing: 12) {
            Spacer().frame(height: 60)
            Image(systemName: "info.circle")
                .resizable()
                .scaledToFit()
                .frame(width: 48, height: 48)
                .foregroundColor(primaryColor)

            Text("Digite um CEP de 8 dígitos para consultar")
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .padding(16)
    }
}

// MARK: - Loading State Content
private struct LoadingStateContent: View {
    var body: some View {
        VStack {
            Spacer().frame(height: 60)
            ProgressView()
                .scaleEffect(1.3)
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .padding(16)
    }
}

// MARK: - Error State Content
private struct ErrorStateContent: View {
    let mensagem: String
    let containerColor: Color
    let textColor: Color
    let errorColor: Color

    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            Image(systemName: "exclamationmark.triangle.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 32, height: 32)
                .foregroundColor(errorColor)

            VStack(alignment: .leading, spacing: 4) {
                Text("Atenção")
                    .font(.headline)
                    .fontWeight(.bold)
                    .foregroundColor(errorColor)

                Text(mensagem)
                    .font(.body)
                    .foregroundColor(textColor)
            }

            Spacer()
        }
        .padding(16)
        .background(containerColor)
        .cornerRadius(12)
        .frame(maxWidth: .infinity)
    }
}

// MARK: - Success State Content
private struct SuccessStateContent: View {
    let endereco: Endereco
    let primaryColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Cabeçalho do Card
            HStack(spacing: 8) {
                Image(systemName: "mappin.circle.fill")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 28, height: 28)
                    .foregroundColor(primaryColor)

                Text("Endereço Encontrado")
                    .font(.title3)
                    .fontWeight(.bold)
                    .foregroundColor(primaryColor)

                Spacer()
            }

            Spacer().frame(height: 12)
            Divider()
            Spacer().frame(height: 12)

            // Itens de Endereço
            EnderecoItem(
                label: "Logradouro",
                valor: endereco.logradouro.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
                    ? "Não informado"
                    : endereco.logradouro
            )

            EnderecoItem(
                label: "Bairro",
                valor: endereco.bairro.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
                    ? "Não informado"
                    : endereco.bairro
            )

            EnderecoItem(
                label: "Cidade/UF",
                valor: formatarCidadeUf(cidade: endereco.cidade, uf: endereco.uf)
            )

            EnderecoItem(
                label: "CEP",
                valor: formatarCep(endereco.cep)
            )
        }
        .padding(16)
        .background(Color(UIColor.secondarySystemBackground))
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.08), radius: 4, x: 0, y: 2)
        .frame(maxWidth: .infinity)
    }

    private func formatarCidadeUf(cidade: String, uf: String) -> String {
        let cidadeTrimmed = cidade.trimmingCharacters(in: .whitespacesAndNewlines)
        let ufTrimmed = uf.trimmingCharacters(in: .whitespacesAndNewlines)
        let raw = "\(cidadeTrimmed) - \(ufTrimmed)"
        return raw.trimmingCharacters(in: CharacterSet(charactersIn: "- "))
    }

    private func formatarCep(_ cep: String) -> String {
        let digitsOnly = cep.filter { $0.isNumber }
        if digitsOnly.count == 8 {
            let start = digitsOnly.prefix(5)
            let end = digitsOnly.suffix(3)
            return "\(start)-\(end)"
        }
        return cep
    }
}

// MARK: - Endereco Item
private struct EnderecoItem: View {
    let label: String
    let valor: String

    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(label)
                .font(.caption)
                .foregroundColor(.secondary)

            Text(valor)
                .font(.body)
                .fontWeight(.medium)
        }
        .padding(.vertical, 4)
    }
}

#Preview("Default (Idle)") {
    CepScreen()
}

#Preview("Sucesso") {
    CepScreen(
        viewModel: CepViewModel(
            initialCep: "01001000",
            initialUiState: .success(
                Endereco(
                    cep: "01001-000",
                    logradouro: "Praça da Sé",
                    bairro: "Sé",
                    cidade: "São Paulo",
                    uf: "SP"
                )
            )
        )
    )
}

#Preview("Erro") {
    CepScreen(
        viewModel: CepViewModel(
            initialCep: "00000000",
            initialUiState: .error("CEP não encontrado.")
        )
    )
}

#Preview("Carregando") {
    CepScreen(
        viewModel: CepViewModel(
            initialCep: "01001000",
            initialUiState: .loading
        )
    )
}