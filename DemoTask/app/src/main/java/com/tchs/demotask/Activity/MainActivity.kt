package com.tchs.demotask.Activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.tchs.demotask.Adapter.CarAdapter
import com.tchs.demotask.R
import com.tchs.demotask.Ratrofit.ServiceGenerator
import com.tchs.demotask.Ratrofit.Utils
import com.tchs.demotask.Response.CarResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val url = "cars"
    private var filterDailog: Dialog? = null
    private var oddeven = 0
    private var byPriceMin: Float? = 0F
    private var byPriceMax: Float? = 10000F
    private var addedyear: Int? = 0
    var carAdapter: CarAdapter? = null
    var carList: ArrayList<CarResponse.Result>? = null
    var carFilter: ArrayList<CarResponse.Result>? = null
    private var serviceGenerator: ServiceGenerator? = null
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv_filter.setOnClickListener(this)

        carList = arrayListOf()
        carFilter = arrayListOf()

        serviceGenerator = ServiceGenerator()
        Utils.showProgress(this)
        serviceGenerator!!.getApi()!!.getApiCall(url).enqueue(object : Callback<CarResponse> {
            override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                Utils.dismissProgress()
                if (response.raw().networkResponse != null) {
                    Log.d(TAG, "onResponse: response is from NETWORK...")
                } else if (response.raw().cacheResponse != null
                    && response.raw().networkResponse == null
                ) {
                    Log.d(TAG, "onResponse: response is from CACHE...")
                }

                carList!!.addAll(response.body()!!.getResult()!! as ArrayList<CarResponse.Result>)
                carFilter!!.addAll(response.body()!!.getResult()!! as ArrayList<CarResponse.Result>)
                setAdapter()
                /*    if (response.body() == null) {
                        setAdapter(response.body()!!.getResult()!! as ArrayList<CarResponse.Result>)
                    } else {*/
                //  }
            }

            override fun onFailure(call: Call<CarResponse>, t: Throwable) {

            }
        })
        sv_find!!.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                carAdapter!!.filter.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                carAdapter!!.filter.filter(query)
                Utils.hideInputSoftKey(this@MainActivity)
                return true
            }

        })

        sv_find!!.setOnCloseListener(object : androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Utils.hideInputSoftKey(this@MainActivity)
                return false
            }
        })

    }

    private fun setAdapter() {
        if (carFilter!!.size > 0) {
            carAdapter = CarAdapter(this, carFilter!!)
            rvc_cars!!.adapter = carAdapter
            carAdapter!!.notifyDataSetChanged()
            rl_no_data.visibility = View.GONE
            rvc_cars.visibility = View.VISIBLE
        } else {
            rl_no_data.visibility = View.VISIBLE
            rvc_cars.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.iv_filter -> {
                filterDialog()
            }
        }
    }

    private fun filterDialog() {
        filterDailog = Dialog(this)
        filterDailog!!.setContentView(R.layout.dialog_filter)
        filterDailog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window: Window? = filterDailog!!.getWindow()
        val wlp: WindowManager.LayoutParams = window!!.getAttributes()
        wlp.gravity = Gravity.BOTTOM

        filterDailog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        filterDailog!!.setCancelable(false)

        val tvOddEven = filterDailog!!.findViewById(R.id.tv_odd_even) as TextView
        val tvPrice = filterDailog!!.findViewById(R.id.tv_price) as TextView
        val tvYear = filterDailog!!.findViewById(R.id.tv_year) as TextView
        val btnClear = filterDailog!!.findViewById(R.id.btn_clear) as TextView
        val btnApply = filterDailog!!.findViewById(R.id.btn_apply) as TextView
        val tvOdd = filterDailog!!.findViewById(R.id.tv_odd) as TextView
        val tvEven = filterDailog!!.findViewById(R.id.tv_even) as TextView
        val tvmin = filterDailog!!.findViewById(R.id.tvmin) as TextView
        val tvmax = filterDailog!!.findViewById(R.id.tvmax) as TextView
        val rangeSeekbar = filterDailog!!.findViewById(R.id.rangeSeekbar) as CrystalRangeSeekbar
        val etYear = filterDailog!!.findViewById(R.id.et_year) as EditText

        val llOddEven = filterDailog!!.findViewById(R.id.ll_odd_even) as LinearLayout
        val llYear = filterDailog!!.findViewById(R.id.ll_year) as LinearLayout
        val llPrice = filterDailog!!.findViewById(R.id.ll_price) as LinearLayout
        val ivClose = filterDailog!!.findViewById(R.id.iv_close) as ImageView

        tvOddEven.setTextColor(getColor(R.color.gray))
        tvPrice.setTextColor(getColor(R.color.black))
        tvYear.setTextColor(getColor(R.color.black))
        llOddEven.visibility = View.VISIBLE
        llPrice.visibility = View.GONE
        llYear.visibility = View.GONE

        if (addedyear != 0) {
            etYear.setText(addedyear.toString())
        }

        if (oddeven != 0) {
            if (oddeven == 1) {
                tvOdd.setBackgroundResource(R.drawable.bg_edittext_white)
                tvEven.setBackgroundResource(R.drawable.bg_edittext_gray)
            } else {
                tvEven.setBackgroundResource(R.drawable.bg_edittext_white)
                tvOdd.setBackgroundResource(R.drawable.bg_edittext_gray)
            }
        }
        rangeSeekbar.setMinValue(0F)
        rangeSeekbar.setMaxValue(10000F)
        rangeSeekbar.setMinStartValue(byPriceMin!!.toFloat())
            .setMaxStartValue(byPriceMax!!.toFloat()).apply()

        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            byPriceMin = minValue.toFloat()
            byPriceMax = maxValue.toFloat()
            tvmin.text = minValue.toString()
            tvmax.text = maxValue.toString()
        }

        tvOddEven.setOnClickListener {
            llOddEven.visibility = View.VISIBLE
            llPrice.visibility = View.GONE
            llYear.visibility = View.GONE
            tvOddEven.setTextColor(getColor(R.color.gray))
            tvPrice.setTextColor(getColor(R.color.black))
            tvYear.setTextColor(getColor(R.color.black))
        }
        tvPrice.setOnClickListener {
            llOddEven.visibility = View.GONE
            llPrice.visibility = View.VISIBLE
            llYear.visibility = View.GONE
            tvPrice.setTextColor(getColor(R.color.gray))
            tvYear.setTextColor(getColor(R.color.black))
            tvOddEven.setTextColor(getColor(R.color.black))
        }
        tvYear.setOnClickListener {
            llOddEven.visibility = View.GONE
            llPrice.visibility = View.GONE
            llYear.visibility = View.VISIBLE
            tvYear.setTextColor(getColor(R.color.gray))
            tvOddEven.setTextColor(getColor(R.color.black))
            tvPrice.setTextColor(getColor(R.color.black))
        }
        tvOdd.setOnClickListener {
            // filterDailog!!.dismiss()
            oddeven = 1
            tvOdd.setBackgroundResource(R.drawable.bg_edittext_white)
            tvEven.setBackgroundResource(R.drawable.bg_edittext_gray)
        }

        tvEven.setOnClickListener {
            // filterDailog!!.dismiss()
            oddeven = 2
            tvEven.setBackgroundResource(R.drawable.bg_edittext_white)
            tvOdd.setBackgroundResource(R.drawable.bg_edittext_gray)
        }
        btnApply.setOnClickListener {

            carFilter!!.clear()
            if (etYear.text.toString().isNotBlank()) {
                addedyear = etYear.text.toString().toInt()
            }
            var priceFilter = false
            var oddenveFilter = false
            var yearfilter = false

            for (item in carList!!) {
                val itemPrice = item.price!!.replace("$", "").toFloat()
                val roudPrice: Int = itemPrice.roundToInt()

                if (byPriceMin != 0F || byPriceMax != 10000F) {
                    priceFilter = true
                }

                if (oddeven != 0) {
                    oddenveFilter = true
                }

                if (addedyear.toString().length == 4) {
                    yearfilter = true
                }

                if (oddenveFilter == true && yearfilter == true && priceFilter == true) {
                    if (oddeven == 1) {
                        if (item.id!! % 2 != 0 && item.carModelYear!! == addedyear && roudPrice >= byPriceMin!! && roudPrice <= byPriceMax!!
                        ) {
                            carFilter!!.add(item)
                        }
                    } else {
                        if (item.id!! % 2 == 0 && item.carModelYear!! == addedyear && roudPrice >= byPriceMin!! && roudPrice <= byPriceMax!!
                        ) {
                            carFilter!!.add(item)
                        }
                    }
                } else if (oddenveFilter == true && yearfilter == true) {
                    if (oddeven == 1) {
                        if (item.id!! % 2 != 0 && item.carModelYear!! == addedyear) {
                            carFilter!!.add(item)
                        }
                    } else {
                        if (item.id!! % 2 == 0 && item.carModelYear!! == addedyear) {
                            carFilter!!.add(item)
                        }
                    }
                } else if (oddenveFilter == true && priceFilter == true) {
                    if (oddeven == 1) {
                        if (item.id!! % 2 != 0 && roudPrice >= byPriceMin!! && roudPrice <= byPriceMax!!
                        ) {
                            carFilter!!.add(item)
                        }
                    } else {
                        if (item.id!! % 2 == 0 && roudPrice >= byPriceMin!! && roudPrice <= byPriceMax!!
                        ) {
                            carFilter!!.add(item)
                        }
                    }
                } else if (yearfilter == true && priceFilter == true) {
                    if (item.carModelYear!! == addedyear && roudPrice >= byPriceMin!! && roudPrice <= byPriceMax!!) {
                        carFilter!!.add(item)
                    }
                } else if (oddenveFilter == true) {
                    if (oddeven == 1) {
                        if (item.id!! % 2 != 0) {
                            carFilter!!.add(item)
                        }
                    } else {
                        if (item.id!! % 2 == 0) {
                            carFilter!!.add(item)
                        }
                    }
                } else if (yearfilter == true) {
                    if (item.carModelYear!! == addedyear) {
                        carFilter!!.add(item)
                    }
                } else if (priceFilter == true) {
                    if (roudPrice >= byPriceMin!! && roudPrice < byPriceMax!!) {
                        carFilter!!.add(item)
                    }
                } else {
                    carFilter!!.addAll(carList!!)
                }
            }
            setAdapter()

            filterDailog!!.dismiss()
        }
        btnClear.setOnClickListener {
            oddeven = 0
            byPriceMin = 0F
            byPriceMax = 10000F
            addedyear = 0
            etYear.setText("")
            rangeSeekbar.setMinStartValue(byPriceMin!!.toFloat())
                .setMaxStartValue(byPriceMax!!.toFloat()).apply()
            tvOdd.setBackgroundResource(R.drawable.bg_edittext_gray)
            tvEven.setBackgroundResource(R.drawable.bg_edittext_gray)
            tvOddEven.setTextColor(getColor(R.color.gray))
            tvPrice.setTextColor(getColor(R.color.black))
            tvYear.setTextColor(getColor(R.color.black))
            llOddEven.visibility = View.VISIBLE
            llPrice.visibility = View.GONE
            llYear.visibility = View.GONE
            carFilter!!.clear()
            carFilter!!.addAll(carList!!)
            setAdapter()

        }
        ivClose.setOnClickListener {
            oddeven = 0
            byPriceMin = 0F
            byPriceMax = 10000F
            addedyear = 0
            etYear.setText("")
            tvOdd.setBackgroundResource(R.drawable.bg_edittext_gray)
            tvEven.setBackgroundResource(R.drawable.bg_edittext_gray)
            rangeSeekbar.setMinStartValue(byPriceMin!!.toFloat())
                .setMaxStartValue(byPriceMax!!.toFloat()).apply()
            carFilter!!.clear()
            carFilter!!.addAll(carList!!)
            setAdapter()
            filterDailog!!.dismiss()
        }
        filterDailog!!.show()
    }
}


