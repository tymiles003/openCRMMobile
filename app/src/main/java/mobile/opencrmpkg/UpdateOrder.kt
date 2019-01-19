package mobile.opencrmpkg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_update_order.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class UpdateOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_order)

        val intent = getIntent()

        val orderNo: String = intent.getStringExtra("orderId")
        textUpdateOrderHeading.setText(orderNo)

        val customerName: String = intent.getStringExtra("customerName")
        textCustomerVal.setText(customerName)

        val status: String = intent.getStringExtra("status")
        editStatus.setText(status)

        val cost: String = intent.getStringExtra("cost")
        editCost.setText(cost)

        val salesPerson: String = intent.getStringExtra("salesPerson")
        editSalesman.setText(salesPerson)


        buttonUpdateOrder.setOnClickListener{
            view -> Snackbar.make(view, "Development in progress", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//            var postParameters = "?customer=${editCustomer.text}&salesman=${editSalesman.text}&cost=${editCost.text}&status=${editStatus.text}";
//            UpdateOrder.Companion.postToGSheet(this).execute(postParameters);
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()


/*        val clearActivity: Intent
        clearActivity = Intent(this, UpdateOrder::class.java)
        clearActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        clearActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(clearActivity)

        val orderIntent = Intent(this, ViewOrders::class.java)
        startActivity(orderIntent)*/
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        class postToGSheet internal constructor(context: UpdateOrder) : AsyncTask<String, String, String>() {

            private var result: String? = null
            private val activityReference: WeakReference<UpdateOrder> = WeakReference(context)


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
                    //Log.d(UpdateOrder?.tag, "responseCode - " + responseCode)

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
//                            Log.e(UpdateOrder?.tag, "Error in convertToString " + Ex.printStackTrace())
                        }
                    }
                } catch (ex: Exception) {
                    publishProgress(activity?.getString(R.string.update_failed_status))
                    //Log.d(UpdateOrder?.tag, "Error in doInBackground " + ex.message)
                }
                return result
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // ...

                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.VISIBLE
                activity.buttonUpdateOrder.isEnabled = false
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result);
                //println("output from google sheet: $result");

                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.GONE
                activity.buttonUpdateOrder.isEnabled = true

                activity.textCustomerVal.text = null
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
