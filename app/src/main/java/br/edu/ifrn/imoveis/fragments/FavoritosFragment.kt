package br.edu.ifrn.imoveis.fragments

import br.edu.ifrn.imoveis.activity.ImovelActivity
import br.edu.ifrn.imoveis.adapter.ImovelAdapter
import br.edu.ifrn.imoveis.domain.Imovel
import br.edu.ifrn.imoveis.domain.FavoritosService
import br.edu.ifrn.imoveis.domain.event.FavoritoEvent
import kotlinx.android.synthetic.main.fragment_imoveis.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class FavoritosFragment : ImoveisFragment() {

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onRefresh(event: FavoritoEvent) {
        taskImoveis()
    }

    override fun taskImoveis() {
        doAsync {
            // Busca os imoveis
            imoveis = FavoritosService.getImoveis()
            uiThread {
                recyclerView.adapter = ImovelAdapter(imoveis) { onClickImovel(it) }

                // Termina a animação do Swipe to Refresh
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    override fun onClickImovel(imovel: Imovel) {
        // Ao clicar no imovel vamos navegar para a tela de detalhes
        activity?.startActivity<ImovelActivity>("imovel" to imovel)
    }
}
