package mobile.opencrmpkg

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.card.MaterialCardView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_view_orders.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection




class ViewOrders : AppCompatActivity() {

    public var gC = getBaseContext();
    private val tag: String = "MainActivity"
    private val spreadsheetResult: String? = null
    private var exit: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        loadData(this).execute();
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if (exit) {

            var clearActivity: Intent
            clearActivity = Intent(this, ViewOrders::class.java)
            clearActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(clearActivity)

            clearActivity = Intent(this, AddOrder::class.java)
            clearActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(clearActivity)

            clearActivity = Intent(this, UpdateOrder::class.java)
            clearActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(clearActivity)

            var mainActivity: Intent
            mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)


        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
        }

    }

    fun goToOrderDetail(orderNo: String, cost: String, status: String, salesPerson: String, customerName: String) {
        val orderIntent = Intent(this, UpdateOrder::class.java)
        orderIntent.putExtra("orderId", orderNo)
        orderIntent.putExtra("cost", cost)
        orderIntent.putExtra("status", status)
        orderIntent.putExtra("salesPerson", salesPerson)
        orderIntent.putExtra("customerName", customerName)
        startActivity(orderIntent);
    }

    @SuppressLint("ResourceAsColor")
    fun createTable(rows: Int, cols: Int, result: JSONArray?) {

        try {
            Log.i(tag,"Output from createTable: $result")
            for (i in 0 until rows) {

                // Convert Input to JsonArray
                var strResult = result?.getJSONArray(i).toString()
                Log.i(tag,"Output from createTable jsonarray to string: $strResult")
                val rowResult: JSONArray = JSONArray(strResult)
                Log.i(tag,"Output from createTable string to jsonarray: $rowResult")

                //Initialise layout
                val orderCardView = MaterialCardView(this)
                val tblLayout = TableLayout(this)
                tblLayout.id = i
                tblLayout.isStretchAllColumns = true

                //setup layout details
                val cvLayoutParam = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orderCardView.layoutParams = cvLayoutParam
                orderCardView.id = i;
                orderCardView.useCompatPadding = true;

                tblLayout.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )

                val trLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )

                val txtVwTableRow = TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 1f)

                //Setup 1st row
                val  tblRow1 = TableRow(this)
                tblRow1.layoutParams = trLayoutParams
                tblRow1.weightSum=2f

                //Order No
                val txtOrderNo = TextView(this)
                txtOrderNo.text = rowResult[0].toString()
                txtOrderNo.layoutParams = txtVwTableRow
                txtOrderNo.setPadding(30,40,0,40)
                txtOrderNo.setTextColor(Color.BLACK)
                txtOrderNo.width= 0
                tblRow1.addView(txtOrderNo)


                //Salesman
                val txtSalesperson = TextView(this)
                txtSalesperson.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0)
                txtSalesperson.text = rowResult[2].toString()
                txtSalesperson.layoutParams = txtVwTableRow
                txtSalesperson.setPadding(0,40,30,40)
                txtSalesperson.setTextColor(Color.BLACK)
                txtSalesperson.gravity = Gravity.CENTER_VERTICAL
                txtSalesperson.width= 0
                tblRow1.addView(txtSalesperson)

                tblLayout.addView(tblRow1)

                //Setup 2nd row
                val  tblRow2 = TableRow(this)
                tblRow2.layoutParams = trLayoutParams
                tblRow2.weightSum=2f

                //Customer
                val txtCustomer = TextView(this)
                //txtCustomer.setTypeface(Typeface.DEFAULT_BOLD)
                txtCustomer.setTextSize(23F)
                txtCustomer.text = rowResult[1].toString()
                val txtVwTableSpanRow = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT)
                txtVwTableSpanRow.span = 2
                txtCustomer.layoutParams = txtVwTableSpanRow
                txtCustomer.setPadding(30,0,30,60)
                txtCustomer.setTextColor(Color.BLACK)
                tblRow2.addView(txtCustomer)

                tblLayout.addView(tblRow2)


                //Setup 3rd row
                val  tblRow3 = TableRow(this)
                tblRow3.layoutParams = trLayoutParams
                tblRow3.weightSum=2f


                //Cost
                val txtCost = TextView(this)
                txtCost.layoutParams = txtVwTableRow
                txtCost.setPadding(0,40,30,40)
                txtCost.text = "Rs." + rowResult[3].toString()
                txtCost.setPadding(30,40,0,40)
                txtCost.width = 0
                txtCost.setTextColor( Color.GRAY)
                tblRow3.addView(txtCost)

                //Status
                val txtStatus = TextView(this)
                txtStatus.text = rowResult[4].toString()
                txtStatus.layoutParams = txtVwTableRow
                txtStatus.setPadding(0,40,30,40)
                txtStatus.width  =0
                txtStatus.setTextColor(Color.parseColor("#ff6600"))
                tblRow3.addView(txtStatus)

                tblLayout.addView(tblRow3)

                //Setup 4th row
                val  tblRow4 = TableRow(this)
                tblRow4.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                tblRow4.setBackgroundColor(Color.parseColor("#e4e4e4"))
                tblRow4.weightSum=2f

                val btnVwTableRow = TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 1f)
                btnVwTableRow.width = 0

                //Cancel Button
                val buttonCancel = Button(this  )
                buttonCancel.setBackgroundColor(Color.TRANSPARENT)
                buttonCancel.setTextColor(Color.parseColor("#e55b70"))
                buttonCancel.layoutParams = btnVwTableRow
                buttonCancel.text = "Cancel"
                buttonCancel.setOnClickListener{
                    //goToOrderDetail(rowResult[0].toString())
                    view -> Snackbar.make(view, "Development in progress", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                }
                tblRow4.addView(buttonCancel)

                //Update Button
                val buttonUpdate = Button(this  )
                buttonUpdate.setBackgroundColor(Color.TRANSPARENT)
                buttonUpdate.setTextColor(Color.parseColor("#3039dd"))
                buttonUpdate.layoutParams = btnVwTableRow
                buttonUpdate.text = "Update"
                buttonUpdate.setOnClickListener{
                    goToOrderDetail(
                        rowResult[0].toString(),
                        rowResult[3].toString(),
                        rowResult[4].toString(),
                        rowResult[2].toString(),
                        rowResult[1].toString()
                    )
                }
                tblRow4.addView(buttonUpdate)


                /*//Mark as Cancel Button
                val buttonCancel =  ImageButton(this)
                buttonCancel.layoutParams = btnVwTableRow
                buttonCancel.adjustViewBounds = true
                buttonCancel.scaleType = ImageView.ScaleType.FIT_XY
                buttonCancel.setBackgroundResource(R.drawable.ic_cancel_black_24dp)
                tblRow4.addView(buttonCancel)

                //Mark as Complete Button
                val buttonComplete = ImageButton(this)
                buttonComplete.adjustViewBounds = true
                buttonComplete.layoutParams = btnVwTableRow
                buttonComplete.scaleType = ImageView.ScaleType.FIT_XY
                buttonComplete.setBackgroundResource(R.drawable.ic_done_black_24dp)
                tblRow4.addView(buttonComplete)*/
//
//                tblRow4.addView(buttonComplete)

                tblLayout.addView(tblRow4)


                /*



                val cancelButton = MaterialButton(this)
                cancelButton.text="CANCEL"
//                cancelButton.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 1f)
//                cancelButton.width = 0
                //cancelButton.setTextColor( Color.parseColor("#fff"))
                //cancelButton.setBackgroundColor(Color.parseColor("#fd6768"))
                tblRow3.addView(cancelButton)

                val markButton = MaterialButton(this)
//                markButton.setTextColor( Color.parseColor("#fff"))
                markButton.text="COMPLETE"
//                markButton.layoutParams = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 1f)
//                markButton.width = 0
                //markButton.setBackgroundColor(Color.parseColor("#98cd65"))
                tblRow3.addView(markButton)

                tblLayout.addView(tblRow3)
*/

                orderCardView.addView(tblLayout)
                linearLayout.addView(orderCardView)

            }
        } catch (ex: Exception) {
            Log.d(tag, "Error in createTable " + ex.message)
        }
        progressBar.visibility = View.GONE
    }

    @SuppressLint("StaticFieldLeak")
    class loadData(private var viewOrders: ViewOrders?) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String? {
            var result = ""
            try {
                val url = URL("https://script.google.com/macros/s/AKfycbxLboi7OM7H7xc-Rwe0QvJVh8jk8HdLAPznItq7E2OOrQmLSYM/exec")
                val httpURLConnection = url.openConnection() as HttpsURLConnection

                httpURLConnection.readTimeout = 8000
                httpURLConnection.connectTimeout = 8000
                httpURLConnection.doOutput = false
                httpURLConnection.connect()

                val responseCode: Int = httpURLConnection.responseCode
                Log.d(viewOrders?.tag, "responseCode - " + responseCode)

                if (responseCode == 200) {
                    val inStream: InputStream = httpURLConnection.inputStream
                    val isReader = InputStreamReader(inStream)
                    val bReader = BufferedReader(isReader)
                    var tempStr: String?

                    try {

                        while (true) {
                            tempStr = bReader.readLine()
                            if (tempStr == null) {
                                break
                            }
                            result += tempStr
                        }
                    } catch (Ex: Exception) {
                        Log.e(viewOrders?.tag, "Error in convertToString " + Ex.printStackTrace())
                    }
                }
            } catch (ex: Exception) {
                Log.d(viewOrders?.tag, "Error in doInBackground " + ex.message)
            }

            return result
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result);
            try {
                Log.i(viewOrders?.tag, "Try in onPostExecute String : $result")
                var jsonResult : JSONArray =  JSONArray(result)
                Log.i(viewOrders?.tag, "Try in onPostExecute JSON : $jsonResult")
                var rowCount = jsonResult.length()
                viewOrders?.createTable(rowCount,5, jsonResult);
            }
            catch (ex: java.lang.Exception) {
                Log.e(viewOrders?.tag, "Error in onPostExecute " + ex.printStackTrace())
            }


        }

    }

    fun goToAddNewOrder(view: View) {
        val addNewOrderIntent = Intent(this, AddOrder::class.java);
        startActivity(addNewOrderIntent);
    }
}
