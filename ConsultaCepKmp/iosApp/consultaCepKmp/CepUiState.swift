//
// Created by João Guilherme Brasil Pichetti on 10/10/26.
//

import Foundation
import sharedKit

enum CepUiState: Equatable {
    case idle
    case loading
    case success(Endereco)
    case error(String)
}