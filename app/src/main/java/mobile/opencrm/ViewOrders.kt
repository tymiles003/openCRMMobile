package mobile.opencrm

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_view_orders.*
import mobile.opencrm.R.attr.colorPrimaryDark
import mobile.opencrm.R.string.view_orders
import org.w3c.dom.Text


class ViewOrders : AppCompatActivity() {

    val tableLayout by lazy { TableLayout(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        val pageHeader = TextView(this );
        pageHeader.setTextColor(colorPrimaryDark)
        pageHeader.setTextSize(24F)
        pageHeader.text = "View Orders"

        constraintLayout.addView(pageHeader)

        val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.apply {
            layoutParams = lp
            isShrinkAllColumns = true
        }

        createTable(10, 5,null);
    }

    fun createTable(rows: Int, cols: Int, result: String?) {

        for(i in 0 until rows) {

            val row = TableRow(this);
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            for(j in 0 until cols)  {

                if(j == 0 && i == 0) {
                    createHeader(cols);
                }

                val rcText = TextView(this);
                rcText.text = "R $i R $j";
                row.addView(rcText);
            }
            tableLayout.addView(row);
        }

        constraintLayout.addView(tableLayout);
    }

    fun createHeader(cols: Int) {

        val row = TableRow(this);
        row.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        for(j in 0 until cols) {
            val rcHeader = TextView(this);
            rcHeader.text = "Header $j";
            row.addView(rcHeader);
        }
        tableLayout.addView(row)
    }

    fun goToAddNewOrder(view: View) {
        val addNewOrderIntent = Intent(this, AddOrder::class.java);
        startActivity(addNewOrderIntent);
    }
}
