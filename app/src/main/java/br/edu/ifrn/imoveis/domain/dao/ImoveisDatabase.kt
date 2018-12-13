package br.edu.ifrn.imoveis.domain.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import br.edu.ifrn.imoveis.domain.Imovel

// Define as classes que precisam ser persistidas e a versão do banco
@Database(entities = arrayOf(Imovel::class), version = 1)
abstract class ImovelDatabase : RoomDatabase() {
    abstract fun imovelDAO(): ImovelDAO
}
