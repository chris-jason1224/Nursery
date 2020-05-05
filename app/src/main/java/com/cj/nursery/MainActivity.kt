package com.cj.nursery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cj.base_common.states.OnEmptyStateCallback
import com.cj.base_common.states.OnPlaceHolderCallback
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadSir

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var loadSir:LoadSir? = LoadSir.getDefault()

        var serv = loadSir!!.register(this,object : Callback.OnReloadListener{
            override fun onReload(v: View?) {
                if(v!=null){

                }
            }

        })

        serv.showCallback(OnPlaceHolderCallback::class.java)


    }
}
