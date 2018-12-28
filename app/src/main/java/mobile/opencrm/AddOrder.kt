package mobile.opencrm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import kotlinx.android.synthetic.main.activity_add_order.*
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
            someTask(this).execute();
        }
    }

  //  fun ToastAddOrder(view: View) {
//      someTask(this).execute();
    //}

    @SuppressLint("StaticFieldLeak")
    class someTask(private var addOrder: AddOrder?) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String? {
            var result = ""
            try {
                val url = URL("https://script.google.com/macros/s/AKfycbxLboi7OM7H7xc-Rwe0QvJVh8jk8HdLAPznItq7E2OOrQmLSYM/exec")
                val httpURLConnection = url.openConnection() as HttpsURLConnection

                httpURLConnection.readTimeout = 8000
                httpURLConnection.connectTimeout = 8000
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
                Log.d("", "Error in doInBackground " + ex.message)
            }
            return result
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result);
            println("output from google sheet:");
            println(result);
//            val myToast = makeText( AddOrder().gC, result + "Success.", Toast.LENGTH_SHORT)
//            myToast.show();
        }

}


}
