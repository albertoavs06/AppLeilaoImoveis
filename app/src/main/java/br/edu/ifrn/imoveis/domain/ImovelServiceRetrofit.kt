package br.edu.ifrn.imoveis.domain

import android.util.Base64
import br.edu.ifrn.imoveis.domain.retrofit.ImoveisREST
import br.edu.ifrn.imoveis.extensions.fromJson
import br.edu.ifrn.imoveis.utils.HttpHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Implementação com Retrofit
 */
object ImovelServiceRetrofit {
    private val BASE_URL = "http://portal.ifrn.edu.br/"
    private var service: ImoveisREST

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(ImoveisREST::class.java)
    }

    // Busca os imoveis por tipo 
    fun getImoveis(tipo: TipoImovel): List<Imovel>? {
        val call = service.getImoveis(tipo.name)
        val imoveis = call.execute().body()
        return imoveis
    }

    // Salva um imovel
    fun save(imovel: Imovel): Response? {
        val call = service.save(imovel)
        val response = call.execute().body()
        return response
    }

    // Deleta um imovel
    fun delete(imovel: Imovel): Response? {
        val call = service.delete(imovel.id)
        val response = call.execute().body()
        return response
    }

    // Upload de Foto
    fun postFoto(file: File): Response? {

        // Converte para Base64
        val bytes = file.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

        val call = service.postFoto(file.name, base64)
        val response = call.execute().body()
        return response
    }
}