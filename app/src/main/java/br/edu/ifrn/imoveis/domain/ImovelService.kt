package br.edu.ifrn.imoveis.domain

import android.util.Base64
import br.edu.ifrn.imovel.ImoveisApplication
import br.edu.ifrn.imovel.domain.dao.DatabaseManager
import br.edu.ifrn.imovel.extensions.fromJson
import br.edu.ifrn.imovel.extensions.toJson
import br.edu.ifrn.imovel.utils.HttpHelper
import java.io.File

/**
 * Implementação com OkHttp
 */
object ImovelService {
    private val BASE_URL = "http://portal.ifrn.edu.br"

    // Busca os imoveis por tipo (clássicos, esportivos ou luxo)
    fun getImoveis(tipo: TipoImovel): List<Imovel> {
        val url = "$BASE_URL/tipo/${tipo.name}"
        val json = HttpHelper.get(url)
        val imoveis = fromJson<List<Imovel>>(json)
        return imoveis
    }

    // Salva um imovel
    fun save(imovel: Imovel): Response {
        // Faz POST do JSON imovel
        val json = HttpHelper.post(BASE_URL, imovel.toJson())
        val response = fromJson<Response>(json)
        return response
    }

    // Deleta um imovel
    fun delete(imovel: Imovel): Response {
        val url = "$BASE_URL/${imovel.id}"
        val json = HttpHelper.delete(url)
        val response = fromJson<Response>(json)
        if(response.isOk()) {
            // Se removeu do servidor, remove dos favoritos
            val dao = DatabaseManager.getImovelDAO()
            dao.delete(imovel)
        }
        return response
    }

    fun postFoto(file: File): Response {
        val url = "$BASE_URL/postFotoBase64"

        // Converte para Base64
        val bytes = file.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

        val params = mapOf("fileName" to file.name, "base64" to base64)

        val json = HttpHelper.postForm(url, params)

        val response = fromJson<Response>(json)

        return response
    }
}