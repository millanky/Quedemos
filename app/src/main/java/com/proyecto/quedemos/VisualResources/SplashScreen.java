package com.proyecto.quedemos.VisualResources;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.proyecto.quedemos.R;
import com.proyecto.quedemos.SinUso.MainActivity4;

/**
 * Created by Usuario on 14/06/2016.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //super hace referencia a la clase madre (Activity)
        setContentView(R.layout.splash); //de donde coge el layout: res-layout-splash

        Thread timerThread = new Thread (){ //nuevo hilo, se ejecuta en background

            public void run() {
                try {
                    sleep(3000); //se duerme 3 segundos y luego hace algo
                }catch (InterruptedException e){ //si en esos 3 segundos no se ejecuta
                    e.printStackTrace();
                }finally { //lanzar la MainActividy, creamos la intencion de lanzarla primero
                    Intent intent = new Intent(SplashScreen.this, MainActivity4.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause(){ //poner en pausa si se lanza este metodo
        super.onPause();
        finish();
    }

}
