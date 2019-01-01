package mobile.opencrm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import kotlinx.android.synthetic.main.activity_add_order.*
import mobile.opencrm.R.id.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AddOrder : AppCompatActivity() {

    public var gC = getBaseContext();
    private val tag: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)



        buttonAddOrder.setOnClickListener{
            var postParameters = "?customer=${editCustomer.text}&salesman=${editSalesman.text}&cost=${editCost.text}&status=${editStatus.text}";
            someTask(this).execute(postParameters);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class someTask(private var addOrder: AddOrder?) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String? {
            var result = ""
            try {

                var postParam = p0[0]
                Log.i(addOrder?.tag, "postParametersValue - $postParam")

                val url = URL("https://script.google.com/macros/s/AKfycbxLboi7OM7H7xc-Rwe0QvJVh8jk8HdLAPznItq7E2OOrQmLSYM/exec$postParam")
                val httpURLConnection = url.openConnection() as HttpsURLConnection

                httpURLConnection.readTimeout = 8000
                httpURLConnection.connectTimeout = 8000
                httpURLConnection.doInput = true
                httpURLConnection.doOutput = true
                httpURLConnection.connect()

                val responseCode: Int = httpURLConnection.responseCode
                Log.d(addOrder?.tag, "responseCode - " + responseCode)

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
                        Log.e(addOrder?.tag, "Error in convertToString " + Ex.printStackTrace())
                    }
                }
            } catch (ex: Exception) {
                Log.d(addOrder?.tag, "Error in doInBackground " + ex.message)
            }
            return result
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result);
            println("output from google sheet: $result");

/*            val myToast = makeText( AddOrder().gC, "Order Added", Toast.LENGTH_SHORT)
            myToast.show();*/
        }

}


}
