package br.edu.ifrn.imoveis.activity
import android.os.Bundle
import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.domain.TipoImovel
import br.edu.ifrn.imoveis.fragments.ImoveisFragment
import br.edu.ifrn.imoveis.extensions.addFragment
import br.edu.ifrn.imoveis.extensions.setupToolbar

class ImoveisActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imoveis)
        // Argumentos: tipo do imovel
        val tipo = intent.getSerializableExtra("tipo") as TipoImovel
        val title = getString(tipo.string)
        // Toolbar: configura o título e o "up navigation"
        setupToolbar(R.id.toolbar, title, true)
        if (savedInstanceState == null) {
            // Adiciona o fragment no layout de marcação
            // Dentre os argumentos que foram passados para a activity, está o tipo do imovel.
            addFragment(R.id.container, ImoveisFragment())
        }
    }
}
