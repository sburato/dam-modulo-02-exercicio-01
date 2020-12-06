package com.sburato.photos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import photosGlobal

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PhotoAdapter(this)
        list_view_photo.adapter = adapter

        list_view_photo.setOnItemLongClickListener {
                adapterView: AdapterView<*>, view: View, position: Int, id: Long ->
            val item = adapter.getItem(position)
            adapter.remove(item)
            photosGlobal.remove(item)
            return@setOnItemLongClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        val adapter = list_view_photo.adapter as PhotoAdapter
        adapter.clear()
        adapter.addAll(photosGlobal)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater;
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item01) {
            this.carregarPhotos()
            val adapter = list_view_photo.adapter as PhotoAdapter
            adapter.clear()
            adapter.addAll(photosGlobal)
            return true
        }

        if (item.itemId == R.id.item02) {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            return true
        }

        if (item.itemId == R.id.item03) {
            val text: String = if (photosGlobal.size == 0)
                "Não existem fotos a serem excluídas" else "Pressionar a foto para poder excluir"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun carregarPhotos() {
        try {
            val fotos = PhotoService().execute().get()
            photosGlobal.clear()
            photosGlobal.addAll(fotos)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}