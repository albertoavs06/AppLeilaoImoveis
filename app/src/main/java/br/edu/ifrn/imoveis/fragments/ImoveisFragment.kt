package br.edu.ifrn.imoveis.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.activity.ImovelActivity
import br.edu.ifrn.imoveis.adapter.ImovelAdapter
import br.edu.ifrn.imoveis.domain.Imovel
import br.edu.ifrn.imoveis.domain.ImovelService
import br.edu.ifrn.imoveis.domain.TipoImovel
import br.edu.ifrn.imoveis.domain.event.SaveImovelEvent
import br.edu.ifrn.imoveis.extensions.toast


import kotlinx.android.synthetic.main.fragment_imoveis.*
import kotlinx.android.synthetic.main.include_progress.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity

import java.util.concurrent.Callable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
open class ImoveisFragment : BaseFragment() {
    private var tipo = TipoImovel.classicos
    protected var imoveis = listOf<Imovel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            tipo = arguments?.getSerializable("tipo") as TipoImovel
        }
        // Registra para receber eventos do bus
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Retorna a view /res/layout/fragment_imoveis.xml
        return inflater.inflate(R.layout.fragment_imoveis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Views
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)

        // Swipe to Refresh
        swipeToRefresh.setOnRefreshListener {
            taskImoveis()
        }
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskImoveis()
    }

    @SuppressLint("CheckResult")
    open fun taskImoveis() {
        progress.visibility = View.VISIBLE

        Observable.fromCallable { ImovelService.getImoveis(tipo) } // busca os imoveis
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                /** onNext **/
                recyclerView.adapter = ImovelAdapter(it) { onClickImovel(it) }

                progress.visibility = View.INVISIBLE
                swipeToRefresh.isRefreshing = false

                toast("BOOM RX!")
            },{
                /** onError **/
                toast("Ocorreu um erro!")
            })

        Observable.fromCallable { ImovelService.getImoveis(tipo) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { /* onNext */ },
                { /* onError */ },
                { /* onComplete */ },
                { /* onSubscribe */ }
            )


        Observable.fromCallable(object: Callable<List<Imovel>> {
            override fun call(): List<Imovel> {
                return ImovelService.getImoveis(tipo)
            }

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Consumer<List<Imovel>> {
                // Implementação do onNext
                override fun accept(imoveis: List<Imovel>) {
                    recyclerView.adapter = ImovelAdapter(imoveis) { onClickImovel(it) }

                    progress.visibility = View.INVISIBLE
                    swipeToRefresh.isRefreshing = false
                }

            }, object: Consumer<Throwable> {
                // Implementação do onError
                override fun accept(t: Throwable?) {
                    toast("Erro ao consultar o servidor: $t")

                    progress.visibility = View.INVISIBLE
                    swipeToRefresh.isRefreshing = false
                }
            })

    }

    /*open fun taskImoveis() {
        // Abre uma thread
        doAsync {
            // Busca os imoveis
            imoveis = ImovelService.getImoveis(tipo)
            // Atualiza a lista na UI Thread
            uiThread {
                recyclerView.adapter = ImovelAdapter(imoveis) { onClickImovel(it) }

                // Termina a animação do Swipe to Refresh
                swipeToRefresh.isRefreshing = false
            }
        }
    }*/

    /*open fun taskImoveis() {
        object : Thread() {
            override fun run() {
                // Busca os imoveis
                imoveis = ImovelService.getImoveis(tipo)
                // Atualiza a lista
                activity?.runOnUiThread(Runnable {
                    // Atualiza a lista
                    recyclerView.adapter = ImovelAdapter(imoveis) { onClickImovel(it) }

                })l
            }
        }.start()
    }*/


    open fun onClickImovel(imovel: Imovel) {
        activity?.startActivity<ImovelActivity>("imovel" to imovel)
    }

    @Subscribe
    open fun onRefresh(event: SaveImovelEvent) {
        taskImoveis()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancela os eventos do bus
        EventBus.getDefault().unregister(this)
    }
}
