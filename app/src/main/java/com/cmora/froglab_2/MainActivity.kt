package com.cmora.froglab_2

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.cmora.froglab_2.database.DatabaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var mp : MediaPlayer = MediaPlayer();
    val REQ_CODE_DB = 1
    val SOLICITUD_PERMISO_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        button01.setOnClickListener {
            lanzarJuego()
        }
        button02.setOnClickListener {
            lanzarAyuda()
        }
        button03.setOnClickListener {
            lanzarPreferencias()
        }
        button04.setOnClickListener {
            lanzarDatabase()
        }
        val animacion = AnimationUtils.loadAnimation(this, R.anim.animation3)
        val animacion2 = AnimationUtils.loadAnimation(this, R.anim.animation4)
        //animacion.setRepeatCount(Animation.INFINITE);
        textView3.startAnimation(animacion);
        textView4.startAnimation(animacion2);
        button01.startAnimation(animacion);
        button02.startAnimation(animacion);
        button03.startAnimation(animacion);
        button04.startAnimation(animacion);

        mp = MediaPlayer.create(this, R.raw.audio);
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("musica", true)) {
            mp.start();
        }
        if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                resources.getString(R.string.permission_justify_writemem),
                SOLICITUD_PERMISO_EXTERNAL_STORAGE, this)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.acercade -> {
                lanzarAcercaDe()
                true
            }
            R.id.action_settings -> {
                lanzarPreferencias()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun lanzarAcercaDe(view: View? = null) {
        val i = Intent(this, AboutActivity::class.java)
        startActivity(i)
    }

    fun lanzarJuego(view: View? = null) {
        val i = Intent(this, Juego::class.java)
        i.putExtra("FROM_DATABASE", false);
        Log.d("MAIN", "lanzarJuego 1")
        startActivity(i)
        Log.d("MAIN", "lanzarJuego 2")
    }
    fun lanzarAyuda(view: View? = null){
        val i = Intent(this, HelpActivity::class.java)
        Log.d("MAIN", "lanzarAyuda 1")
        startActivity(i)
        Log.d("MAIN", "lanzarAyuda 2")
    }
    fun lanzarDatabase(view: View? = null){
        Log.d("MAIN", "lanzarDatabaseActivity 1")
        val i = Intent(this, DatabaseActivity::class.java)
        startActivityForResult(i, REQ_CODE_DB);
    }
    fun lanzarPreferencias(view: View? = null) {
        val i = Intent(this, PreferencesActivity::class.java)
        startActivity(i)
        Log.d("MAIN", "lanzarDatabaseActivity 2")
    }
    override fun onPause() {
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("musica", true)) {
            mp.pause();
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("musica", true)) {
            mp.start();
        }
        val animacion = AnimationUtils.loadAnimation(this, R.anim.animation3)
        val animacion2 = AnimationUtils.loadAnimation(this, R.anim.animation4)
        //animacion.setRepeatCount(Animation.INFINITE);
        textView3.startAnimation(animacion);
        textView4.startAnimation(animacion2);
        button01.startAnimation(animacion);
        button02.startAnimation(animacion);
        button03.startAnimation(animacion);
        button04.startAnimation(animacion);
    }

    // override fun onDestroy() {
    //mp.stop();
    //super.onDestroy()
    //}
    override fun onSaveInstanceState(estadoGuardado: Bundle) {
        super.onSaveInstanceState(estadoGuardado)
        if (mp != null) {
            val pos = mp.getCurrentPosition()
            estadoGuardado.putInt("posicion", pos)
        }
    }
    override fun onRestoreInstanceState(estadoGuardado: Bundle?) {
        super.onRestoreInstanceState(estadoGuardado)
        if (estadoGuardado != null && mp != null) {
            val pos = estadoGuardado.getInt("posicion")
            mp.seekTo(pos)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == REQ_CODE_DB){
            Log.d("MAIN OnActivityResult", "lanzarJuego DB 1")
            val i = Intent(this, Juego::class.java)
            i.putExtra("FROM_DATABASE", true);
            i.putExtra("female_hap1", data?.extras?.getString("female_hap1"));
            i.putExtra("female_hap2", data?.extras?.getString("female_hap2"));
            i.putExtra("male_hap1", data?.extras?.getString("male_hap1"));
            i.putExtra("male_hap2", data?.extras?.getString("male_hap2"));
            i.putExtra("genome_name", data?.extras?.getString("genome_name"));
            Log.d("MAIN OnActivityResult", "lanzarJuego DB 3")
            startActivity(i)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("CARLOS", "onRequestPermissionsResult 1")
        if (requestCode == SOLICITUD_PERMISO_EXTERNAL_STORAGE) {
            Log.d("CARLOS", "onRequestPermissionsResult 2")
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("CARLOS", "onRequestPermissionsResult 3")
                Toast.makeText(
                    this,
                    getResources().getString(R.string.permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Log.d("CARLOS", "onRequestPermissionsResult 4")
            Toast.makeText(
                this,
                getResources().getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d("CARLOS", "onRequestPermissionsResult 5")
    }
    fun solicitarPermiso(permiso: String, justificacion: String?, requestCode: Int, actividad: Activity?) {
        Log.d("CARLOS", "Sol. permiso 1")
        val permissions = arrayOf(permiso)
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                actividad!!, permiso)) {
            Log.d("CARLOS", "Sol. permiso 2")
            AlertDialog.Builder(actividad)
                .setTitle(getResources().getString(R.string.permission_required))
                .setMessage(justificacion)
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        ActivityCompat.requestPermissions(
                            actividad,
                            permissions, requestCode
                        )
                    }).show()
            Log.d("CARLOS", "Sol. permiso 3")
        } else {
            Log.d("CARLOS", "Sol. permiso 4")
            ActivityCompat.requestPermissions(
                actividad,
                permissions, requestCode
            )
            Log.d("CARLOS", "Sol. permiso 5")
        }
        Log.d("CARLOS", "Sol. permiso 6")
    }
}