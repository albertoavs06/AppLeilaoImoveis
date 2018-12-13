package br.edu.ifrn.imoveis.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.edu.ifrn.imoveis.R
import br.edu.ifrn.imoveis.domain.Imovel
import br.edu.ifrn.imoveis.domain.ImovelService
import br.edu.ifrn.imoveis.domain.TipoImovel
import br.edu.ifrn.imoveis.extensions.*
import br.edu.ifrn.imoveis.utils.CameraHelper
import kotlinx.android.synthetic.main.activity_imovel_form.*
import kotlinx.android.synthetic.main.activity_imovel_form_contents.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ImovelFormActivity : AppCompatActivity() {
    private val camera = CameraHelper()
    // O Imovel pode ser nulo no caso de um Novo Imovel
    val imovel:Imovel? by lazy { intent.getParcelableExtra<Imovel>("imovel") }
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_imovel_form)
        // Título da Toolbar (Nome do imovel ou Novo Imovel)
        setupToolbar(R.id.toolbar, imovel?.nome?: getString(R.string.novo_imovel))
        // Atualiza os dados do formulário
        initViews()
        // Inicia a camera
        camera.init(icicle)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Salva o estado do arquivo caso gire a tela
        camera.onSaveInstanceState(outState)
    }
    // Inicializa as views
    fun initViews() {
        // Ao clicar no header da foto abre a câmera
        appBarImg.onClick { onClickAppBarImg() }
        // A função apply somente é executada se o objeto não for nulo
        imovel?.apply {
            // Foto do imovel
            appBarImg.loadUrl(imovel?.urlFoto)
            // Dados do imovel
            tDesc.string = desc
            tNome.string = nome
            // Tipo do imovel
            when(tipo) {
                "classicos" -> radioTipo.check(R.id.tipoClassico)
                "esportivos" -> radioTipo.check(R.id.tipoEsportivo)
                "luxo" -> radioTipo.check(R.id.tipoLuxo)
            }
        }
    }
    // Adiciona as opções Salvar e Deletar no menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form_imovel, menu)
        return true
    }
    // Trata os eventos do menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_salvar -> taskSalvar()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun taskSalvar() {
        if(tNome.isEmpty()) {
            // Valida se o campo nome foi preenchido
            tNome.error = getString(R.string.msg_error_form_nome)
            return
        }
        if(tDesc.isEmpty()) {
            // Valida se o campo descrição foi preenchido
            tDesc.error = getString(R.string.msg_error_form_desc)
            return
        }
        val dialog = ProgressDialog.show(this, getString(R.string.app_name), "Salvando o imovel, aguarde...",
                false, true)
        doAsync {
            // Cria um imovel para salvar/atualizar
            val c = imovel?: Imovel()
            // Copia valores do form para o Imovel
            c.nome = tNome.string
            c.desc = tDesc.string
            c.tipo = when (radioTipo.checkedRadioButtonId) {
                R.id.tipoClassico -> TipoImovel.classicos.name
                R.id.tipoEsportivo -> TipoImovel.esportivos.name
                else -> TipoImovel.luxo.name
            }
            // Se tiver foto, faz upload
            val file = camera.file
            if (file != null && file.exists()) {
                val response = ImovelService.postFoto(file)
                if (response.isOk()) {
                    // Atualiza a URL da foto no imovel
                    c.urlFoto = response.url
                }
            }
            // Salva o imovel no servidor
            val response = ImovelService.save(c)
            uiThread {
                // Mensagem com a resposta do servidor
                toast(response.msg)
                dialog.dismiss()
                finish()
            }
        }
    }
    // Ao clicar na imagem do AppHeader abre a câmera
    private fun onClickAppBarImg() {
        val ms = System.currentTimeMillis()
        // Nome do arquivo da foto
        val fileName = if (imovel != null)
            "foto_imovel_${imovel?.id}.jpg" else "foto_imovel_${ms}.jpg"
        // Abre a câmera
        val intent = camera.open(this, fileName)
        startActivityForResult(intent, 0)
    }
    // Lê a foto quando a câmera retornar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Resize da imagem
            val bitmap = camera.getBitmap(600, 600)
            if (bitmap != null) {
                // Salva arquivo neste tamanho
                camera.save(bitmap)
                // Mostra a foto do imovel
                appBarImg.setImageBitmap(bitmap)
            }
        }
    }
}
