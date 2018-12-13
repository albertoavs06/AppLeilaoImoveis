package br.edu.ifrn.imoveis.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.R.id.fab
import br.edu.ifrn.imoveis.domain.Imovel
import br.edu.ifrn.imoveis.domain.ImovelService
import br.edu.ifrn.imoveis.domain.FavoritosService
import br.edu.ifrn.imoveis.domain.event.FavoritoEvent
import br.edu.ifrn.imoveis.domain.event.SaveImovelEvent
import br.edu.ifrn.imoveis.extensions.*
import br.edu.ifrn.imoveis.fragments.MapaFragment
import br.edu.ifrn.imoveis.utils.HttpHelper.post
import kotlinx.android.synthetic.main.activity_imovel.*
import kotlinx.android.synthetic.main.activity_imovel_contents.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*

class ImovelActivity : BaseActivity() {
    val imovel by lazy { intent.getParcelableExtra<Imovel>("imovel") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imovel)
        // Seta o nome do imovel como título da Toolbar
        setupToolbar(R.id.toolbar, imovel.nome, true)
        // Atualiza os dados do imovel na tela
        initViews()
        // Variável gerada automaticamente pelo Kotlin Extensions
        fab.setOnClickListener { onClickFavoritar(imovel) }
    }

    fun initViews() {
        // Variáveis  geradas automaticamente pelo Kotlin Extensions (veja import)
        tDesc.text = imovel.desc
        appBarImg.loadUrl(imovel.urlFoto)

        // Foto do imovel
        img.loadUrl(imovel.urlFoto)

        // Toca o Vídeo
        imgPlayVideo.setOnClickListener {
            val url = imovel.urlVideo
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "video/*")
            startActivity(intent)
        }

        // Adiciona o fragment do Mapa
        val mapaFragment = MapaFragment()
        mapaFragment.arguments = intent.extras
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mapaFragment, mapaFragment)
                .commit()
    }

    override fun onResume() {
        super.onResume()
        taskUpdateFavoritoColor()
    }

    // Busca no banco se o imovel está favoritado e atualiza a cor do FAB
    private fun taskUpdateFavoritoColor() {
        doAsync {
            val b = FavoritosService.isFavorito(imovel)
            uiThread {
                setFavoriteColor(b)
            }
        }
    }

    // Desenha a cor do FAB conforme está favoritado ou não.
    fun setFavoriteColor(favorito: Boolean) {
        // Troca a cor conforme o status do favoritos
        val fundo = ContextCompat.getColor(this, if (favorito) R.color.favorito_on else R.color.favorito_off)
        val cor = ContextCompat.getColor(this, if (favorito) R.color.yellow else R.color.favorito_on)
        fab.backgroundTintList = ColorStateList(arrayOf(intArrayOf(0)), intArrayOf(fundo))
        fab.setColorFilter(cor)
    }

    // Adiciona ou Remove o imovel dos Favoritos
    fun onClickFavoritar(imovel: Imovel) {
        doAsync {
            val favoritado = FavoritosService.favoritar(imovel)
            uiThread {
                // Alerta de sucesso
                toast(if (favoritado) R.string.msg_imovel_favoritado else R.string.msg_imovel_desfavoritado)

                // Atualiza cor do botão FAB
                setFavoriteColor(favoritado)

                // Dispara um evento para atualizar a lista
                EventBus.getDefault().post(FavoritoEvent(imovel))
            }
        }
    }

    // Adiciona as opções Salvar e Deletar no menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_imovel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Trata os eventos do menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_editar -> {
                startActivity<ImovelFormActivity>("imovel" to imovel)
                finish()
            }
            R.id.action_deletar -> {
                alert(R.string.msg_confirma_excluir_imovel, R.string.app_name) {
                    positiveButton(R.string.sim) {
                        // Confirmou o excluir
                        taskExcluir()
                    }
                    negativeButton(R.string.nao) {
                        // Não confirmou...
                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Exclui um imovel do servidor
    fun taskExcluir() {
        doAsync {
            val response = ImovelService.delete(imovel)

            uiThread {
                toast(response.msg)
                finish()

                // Atualiza a lista
                EventBus.getDefault().post(SaveImovelEvent(imovel))
            }
        }
    }
}
