package br.edu.ifrn.imoveis.domain.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import br.edu.ifrn.imoveis.domain.Imovel

@Dao
interface ImovelDAO {
    @Query("SELECT * FROM imovel where id = :id")
    fun getById(id: Long): Imovel?

    @Query("SELECT * FROM imovel")
    fun findAll(): List<Imovel>

    @Insert
    fun insert(imovel: Imovel)

    @Delete
    fun delete(imovel: Imovel)
}