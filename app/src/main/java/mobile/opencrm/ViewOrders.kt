package mobile.opencrm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ViewOrders : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)
    }

    fun goToAddNewOrder(view: View) {
        val addNewOrderIntent = Intent(this, AddOrder::class.java);
        startActivity(addNewOrderIntent);
    }
}
