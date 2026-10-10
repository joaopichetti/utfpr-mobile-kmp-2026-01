//
// Created by João Guilherme Brasil Pichetti on 10/10/26.
//

import Foundation
import sharedKit
import SwiftUI
import Combine

@MainActor
class CepViewModel: ObservableObject {
    @Published var uiState: CepUiState
    @Published var cepInput: String

    private let repository: CepRepository

    init(
        repository: CepRepository = CepRepositoryImpl(),
        initialCep: String = "",
        initialUiState: CepUiState = .idle
    ) {
        self.repository = repository
        self.cepInput = initialCep
        self.uiState = initialUiState
    }

    func onCepChange(_ newCep: String) {
        let sanitized = String(newCep.filter { $0.isNumber }.prefix(8))
        cepInput = sanitized
    }

    func buscarCep() {
        let cep = cepInput.trimmingCharacters(in: .whitespacesAndNewlines)
        if cep.count != 8 {
            withAnimation(.easeInOut(duration: 0.25)) {
                uiState = .error("Digite um CEP válido com 8 dígitos.")
            }
            return
        }

        withAnimation(.easeInOut(duration: 0.25)) {
            uiState = .loading
        }

        Task {
            do {
                let endereco = try await repository.buscarCepOrThrow(cep: cep)
                withAnimation(.easeInOut(duration: 0.25)) {
                    self.uiState = .success(endereco)
                }
            } catch {
                let message = error.localizedDescription.isEmpty
                    ? "Ocorreu um erro ao consultar o CEP." : error.localizedDescription
                withAnimation(.easeInOut(duration: 0.25)) {
                    self.uiState = .error(message)
                }
            }
        }
    }
}