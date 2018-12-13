package br.edu.ifrn.imoveis.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.adapter.TabsAdapter
import br.edu.ifrn.imoveis.domain.TipoImovel
import br.edu.ifrn.imoveis.extensions.setupToolbar
import br.edu.ifrn.imoveis.extensions.toast
import br.edu.ifrn.imoveis.utils.Prefs
import kotlinx.android.synthetic.main.activity_main.*

import org.jetbrains.anko.startActivity


class MainActivity : BaseActivity() , NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar)
        setupNavDrawer()
        setupViewPagerTabs()

        // FAB (variável fab gerada automaticamente pelo Kotlin Extensions)
        fab.setOnClickListener {
            startActivity<ImovelFormActivity>()
        }
    }

    // Configura o Navigation Drawer
    private fun setupNavDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }
    private fun setupViewPagerTabs() {
        // Configura o ViewPager + Tabs
        // As variáveis viewPager e tabLayout são geradas automaticamente pelo Kotlin Extensions
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = TabsAdapter(context, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        // Cor branca no texto (o fundo azul é definido no layout)
        val cor = ContextCompat.getColor(context, R.color.white)
        tabLayout.setTabTextColors(cor, cor)

        // Salva e Recupera a última Tab acessada.
        val tabIdx = Prefs.tabIdx

        viewPager.currentItem = tabIdx
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) { }
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageSelected(page: Int) {
                // Salva o índice da página/tab selecionada
                Prefs.tabIdx = page
            }
        })
    }

    // Trata o evento do Navigation Drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_item_imoveis_todos -> {
                toast("Clicou em imoveis")
            }
            R.id.nav_item_imoveis_classicos -> {
                startActivity<ImoveisActivity>("tipo" to TipoImovel.classicos)
            }
            R.id.nav_item_imoveis_esportivos -> {
                startActivity<ImoveisActivity>("tipo" to TipoImovel.esportivos)
            }
            R.id.nav_item_imoveis_luxo -> {
                startActivity<ImoveisActivity>("tipo" to TipoImovel.luxo)
            }
            R.id.nav_item_site_ifrn -> {
                startActivity<SiteIfrnActivity>()
            }
            R.id.nav_item_settings -> {
                toast("Clicou em configurações")
            }
        }
        // Fecha o menu depois de tratar o evento
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
