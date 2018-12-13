package br.edu.ifrn.imoveis.domain

import br.edu.ifrn.imoveis.R

enum class TipoImovel(val string: Int) {
    classicos(R.string.classicos),
    esportivos(R.string.esportivos),
    luxo(R.string.luxo),
    favoritos(R.string.favoritos)

}
