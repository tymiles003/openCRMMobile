package mobile.opencrm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class testScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_screen)
/*
       *//* for(i: Int in 0 until 5)
        {*//*
            var cd = CardView(this)
            var lp: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            cd.layoutParams = lp
            val pageHeaderText = getString(R.string.view_orders);
            val pageHeader = TextView(this )
            pageHeader.setTextColor(R.attr.colorAccent)
            pageHeader.setTextSize(20F)
            pageHeader.setText(pageHeaderText)
            pageHeader.setPadding(0,5,0,10)

            cd.addView(cd)

            constraintLayout.addView(cd,lp)
        //}*/




    }
}
