package br.edu.ifrn.imoveis.adapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.domain.Imovel
import br.edu.ifrn.imoveis.extensions.loadUrl
import kotlinx.android.synthetic.main.adapter_imovel.view.*
// Define o construtor que recebe (imoveis,onClick)
class ImovelAdapter(
        val imoveis: List<Imovel>,
        val onClick: (Imovel) -> Unit) :
        RecyclerView.Adapter<ImovelAdapter.ImoveisViewHolder>() {
    // Retorna a quantidade de imoveis na lista
    override fun getItemCount(): Int {
        return this.imoveis.size
    }
    // Infla o layout do adapter e retorna o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImoveisViewHolder {
        // Infla a view do adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_imovel, parent, false)
        // Retorna o ViewHolder que contém todas as views
        val holder = ImoveisViewHolder(view)
        return holder
    }
    // Faz o bind para atualizar o valor das views com os dados do Imovel
    override fun onBindViewHolder(holder: ImoveisViewHolder, position: Int) {
        // Recupera o objeto imovel
        val imovel = imoveis[position]
        val view = holder.itemView
        with(view) {
            // Atualiza os dados do imovel
            tNome.text = imovel.nome
            // Faz o download da foto e mostra o ProgressBar
            img.loadUrl(imovel.urlFoto, progress)
            // Adiciona o evento de clique na linha
            setOnClickListener { onClick(imovel) }
        }
    }
    // ViewHolder fica vazio pois usamos o import do Android Kotlin Extensions
    class ImoveisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}
