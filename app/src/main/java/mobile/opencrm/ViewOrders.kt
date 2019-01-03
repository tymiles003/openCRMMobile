package mobile.opencrm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.loopj.android.http.AsyncHttpClient.log
import kotlinx.android.synthetic.main.activity_view_orders.*
import mobile.opencrm.R.attr.colorPrimaryDark
import mobile.opencrm.R.string.view_orders
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class ViewOrders : AppCompatActivity() {

    public var gC = getBaseContext();
    private val tag: String = "MainActivity"
    private val spreadsheetResult: String? = null

    val tableLayout by lazy { TableLayout(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.apply {
            layoutParams = lp
        }


        loadData(this).execute();
    }

    fun createTable(rows: Int, cols: Int, result: JSONArray?) {

        try {
            Log.i(tag,"Output from createTable: $result")
            for (i in 0 until rows) {

                var strResult = result?.getJSONArray(i).toString()
                Log.i(tag,"Output from createTable jsonarray to string: $strResult")

                val rowResult: JSONArray = JSONArray(strResult)
                Log.i(tag,"Output from createTable string to jsonarray: $rowResult")

                val row = TableRow(this);
                row.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                for (j in 0 until cols) {

                    if (j == 0 && i == 0) {
                        createPageHeader()
                        createHeader(cols+1)
                    }

                    val rcText = TextView(this);

                    var rowColResult: String = rowResult?.getString(j).toString()
                    Log.i(tag,"Output from GoogleSheet: $rowColResult")
                    rcText.text = rowColResult
                    row.addView(rcText);
                }
                tableLayout.addView(row);
            }
        } catch (ex: Exception) {
            Log.d(tag, "Error in createTable " + ex.message)
        }
        progressBar.visibility = View.GONE
        horizontalView.addView(tableLayout)
    }

    fun createHeader(cols: Int) {

        val row = TableRow(this);
        row.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        for(j in 0 until cols) {
            val rcHeader = TextView(this);
            /*rcHeader.width = 25*/
            when (j) {
                0 -> rcHeader.text = getString(R.string.column_1)
                1 -> rcHeader.text = getString(R.string.column_2)
                2 -> rcHeader.text = getString(R.string.column_3)
                3 -> rcHeader.text = getString(R.string.column_4)
                3 -> rcHeader.text = getString(R.string.column_5)
            }
            row.addView(rcHeader);
        }
        tableLayout.addView(row)
    }


    fun createPageHeader() {

        val row = TableRow(this);
        row.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val pageHeaderText = getString(R.string.view_orders);
        val pageHeader = TextView(this )
        pageHeader.setTextColor(colorPrimaryDark)
        pageHeader.setTextSize(24F)
        pageHeader.setText(pageHeaderText)
        row.addView(pageHeader)
        tableLayout.addView(row)
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
