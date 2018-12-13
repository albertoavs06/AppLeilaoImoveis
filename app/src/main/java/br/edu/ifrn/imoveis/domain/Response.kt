package br.edu.ifrn.imoveis.domain

data class Response (val id:Long,val status:String,val msg:String,val url:String) {
    fun isOk() = "OK".equals(status, ignoreCase = true)
}