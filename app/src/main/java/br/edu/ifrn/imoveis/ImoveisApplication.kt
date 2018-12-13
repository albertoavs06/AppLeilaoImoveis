package br.edu.ifrn.imoveis

import android.support.multidex.MultiDexApplication
import android.util.Log
import java.lang.IllegalStateException

/**
 * MultiDexApplication:
 * https://developer.android.com/studio/build/multidex.html?hl=pt-br
 */
class ImoveisApplication : MultiDexApplication() {
    private val TAG = "ImoveisApplication"

    // Chamado quando o Android criar o processo da aplicação
    override fun onCreate() {
        super.onCreate()
        // Salva a instância para termos acesso como Singleton
        appInstance = this
    }

    companion object {
        // Singleton da classe Application
        private var appInstance: ImoveisApplication? = null

        fun getInstance(): ImoveisApplication {
            if (appInstance == null) {
                throw IllegalStateException("Configure a classe de Application no AndroidManifest.xml")
            }
            return appInstance!!
        }
    }

    // Chamado quando o Android finalizar o processo da aplicação
    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "ImoveisApplication.onTerminate()")
    }
}
