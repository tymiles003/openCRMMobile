package mobile.opencrm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class AddOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
    }

    fun ToastAddOrder(view: View) {
        val myToast = Toast.makeText(this, "New Order Added. Order No. 7629.", Toast.LENGTH_SHORT)
        myToast.show();
    }
}
