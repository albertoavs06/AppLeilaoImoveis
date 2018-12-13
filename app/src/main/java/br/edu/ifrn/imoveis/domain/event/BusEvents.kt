package br.edu.ifrn.imoveis.domain.event

import br.edu.ifrn.imoveis.domain.Imovel

data class SaveImovelEvent(val imovel: Imovel)

data class FavoritoEvent(val imovel: Imovel)
