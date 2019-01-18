package mobile.opencrm

import android.annotation.SuppressLint
import android.content.Context
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
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AddOrder : AppCompatActivity() {

    //public var gC = getBaseContext();
//    public val tag: String = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)

        val intent = getIntent()
        val orderNo: String = intent.getStringExtra("orderId")
        editCustomer.setText(orderNo)

        buttonAddOrder.setOnClickListener{
            var postParameters = "?customer=${editCustomer.text}&salesman=${editSalesman.text}&cost=${editCost.text}&status=${editStatus.text}";
            postToGSheet(this).execute(postParameters);
        }


    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        class postToGSheet internal constructor(context: AddOrder) : AsyncTask<String, String, String>() {

            private var result: String? = null
            private val activityReference: WeakReference<AddOrder> = WeakReference(context)


            override fun doInBackground(vararg p0: String?): String? {
                val activity = activityReference.get()
                publishProgress(activity?.getString(R.string.update_in_progress))
                try {

                    var postParam = p0[0]
                    //Log.i(tag, "postParametersValue - $postParam")

                    val url = URL("https://script.google.com/macros/s/AKfycbxLboi7OM7H7xc-Rwe0QvJVh8jk8HdLAPznItq7E2OOrQmLSYM/exec$postParam")
                    val httpURLConnection = url.openConnection() as HttpsURLConnection

                    httpURLConnection.readTimeout = 8000
                    httpURLConnection.connectTimeout = 8000
                    httpURLConnection.doInput = true
                    httpURLConnection.doOutput = true
                    httpURLConnection.connect()

                    val responseCode: Int = httpURLConnection.responseCode
                    //Log.d(addOrder?.tag, "responseCode - " + responseCode)

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

//                            publishProgress(activity?.getString(R.string.order_complete_status))
                        } catch (Ex: Exception) {
                            publishProgress(activity?.getString(R.string.update_failed_status))
//                            Log.e(addOrder?.tag, "Error in convertToString " + Ex.printStackTrace())
                        }
                    }
                } catch (ex: Exception) {
                    publishProgress(activity?.getString(R.string.update_failed_status))
                    //Log.d(addOrder?.tag, "Error in doInBackground " + ex.message)
                }
                return result
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...

                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.VISIBLE
                activity.buttonAddOrder.isEnabled = false
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result);
                //println("output from google sheet: $result");

                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.GONE
                activity.buttonAddOrder.isEnabled = true

                activity.editCustomer.text = null
                activity.editStatus.text = null
                activity.editCost.text = null
                activity.editSalesman.text = null

                publishProgress(activity?.getString(R.string.order_complete_status))
                Thread.sleep(4_000)

                val viewOrderIntent = Intent(activity, ViewOrders::class.java)
                activity.startActivity(viewOrderIntent)
            }

            override fun onProgressUpdate(vararg values: String?) {
                super.onProgressUpdate(*values)

                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return

                Toast.makeText(activity, values.firstOrNull(), Toast.LENGTH_SHORT).show()
            }



        }
    }


}
