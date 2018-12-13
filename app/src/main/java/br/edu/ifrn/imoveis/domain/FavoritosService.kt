package br.edu.ifrn.imoveis.domain
import br.edu.ifrn.imoveis.ImoveisApplication
import br.edu.ifrn.imoveis.domain.dao.DatabaseManager

object FavoritosService {
    // Retorna todos os imoveis favoritados
    fun getImoveis(): List<Imovel> {
        val dao = DatabaseManager.getImovelDAO()
        val imoveis = dao.findAll()
        return imoveis
    }
    // Verifica se um imovel está favoritado
    fun isFavorito(imovel: Imovel) : Boolean {
        val dao = DatabaseManager.getImovelDAO()
        val exists = dao.getById(imovel.id) != null
        return exists
    }

    // Salva o imovel ou deleta
    fun favoritar(imovel: Imovel): Boolean {
        val favorito = isFavorito(imovel)
        val dao = DatabaseManager.getImovelDAO()
        if(favorito) {
            // Remove dos favoritos
            dao.delete(imovel)
            return false
        }
        // Adiciona nos favoritos
        dao.insert(imovel)
        return true
    }

}
