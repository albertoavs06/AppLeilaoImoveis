package br.edu.ifrn.imoveis.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.edu.ifrn.imoveis.domain.TipoImovel
import br.edu.ifrn.imoveis.fragments.ImoveisFragment

import br.edu.ifrn.imoveis.fragments.FavoritosFragment

class TabsAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    // Qtde de Tabs
    override fun getCount() = 4

    // Retorna o tipo pela posição
    fun getTipoImovel(position: Int) = when (position) {
        0 -> TipoImovel.classicos
        1 -> TipoImovel.esportivos
        2 -> TipoImovel.luxo
        else -> TipoImovel.favoritos
    }

    // Título da Tab
    override fun getPageTitle(position: Int): CharSequence {
        val tipo = getTipoImovel(position)
        return context.getString(tipo.string)
    }

    // Fragment com a lista de imoveis
    override fun getItem(position: Int): Fragment {
        if (position == 3) {
            // Favoritos
            return FavoritosFragment()
        }
        // Casa, Terreno e Apartamento
        val tipo = getTipoImovel(position)
        val f: Fragment = ImoveisFragment()
        val arguments = Bundle()
        arguments.putSerializable("tipo", tipo)
        f.arguments = arguments
        return f
    }
}