package br.edu.ifrn.imoveis.domain.dao

import android.arch.persistence.room.Room
import br.edu.ifrn.imoveis.ImoveisApplication

object DatabaseManager {
    // Singleton do Room: banco de dados
    private var dbInstance: ImovelDatabase

    init {
        val appContext = ImoveisApplication.getInstance().applicationContext

        // Configura o Room
        dbInstance = Room.databaseBuilder(
                appContext,
                ImovelDatabase::class.java,
                "imoveis.sqlite")
                .build()
    }

    fun getImovelDAO(): ImovelDAO {
        return dbInstance.imovelDAO()
    }
}