package br.edu.ifrn.imoveis.domain.retrofit

import br.edu.ifrn.imoveis.domain.Imovel
import br.edu.ifrn.imoveis.domain.Response
import retrofit2.Call
import retrofit2.http.*

interface ImoveisREST {

    @GET("tipo/{tipo}")
    fun getImoveis(@Path("tipo") tipo: String): Call<List<Imovel>>

    @POST("./")
    fun save(@Body imovel: Imovel): Call<Response>

    @DELETE("{id}")
    fun delete(@Path("id") id: Long): Call<Response>

    @FormUrlEncoded
    @POST("postFotoBase64")
    fun postFoto(@Field("fileName") fileName:String, @Field("base64") base64:String): Call<Response>
}