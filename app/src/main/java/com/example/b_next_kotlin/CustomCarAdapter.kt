/*
 * Copyright (c) 2022.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.b_next_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import model.Car
import model.User

class CustomCarAdapter// pass the context and arrayList for the super
// constructor of the ArrayAdapter class
// invoke the suitable constructor of the ArrayAdapter class
    (context: Context, var availableCars: ArrayList<Car>, var user: User?) :
    ArrayAdapter<Car?>(context, 0, availableCars as List<Car?>) {
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // convertView which is recyclable view
        var currentItemView = convertView

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context).inflate(R.layout.custom_list_view, parent, false)
        }
        val currentCar = getItem(position)
        // quindi li assegno alla text view
        val textView1 = currentItemView!!.findViewById<TextView>(R.id.textView1)
        textView1.text = currentCar!!.name + " " + currentCar.carModel


        // assegno il valore del commento alla seconda text view
        val textView2 = currentItemView.findViewById<TextView>(R.id.textView2)
        textView2.text = """
              Battery: ${currentCar.battery}
              ${currentCar.priceHour}€/h
              """.trimIndent()


        // then according to the position of the view assign the desired image for the same
        val userImage = currentItemView.findViewById<ImageView>(R.id.imageView)
        //assert currentNumberPosition != null;
        userImage.setImageResource(R.drawable.tesla_model_x)
        // Così quando clicco su una macchina mi manda alla pagina delle specifiche
        currentItemView.setOnClickListener { view: View ->
            val intent = Intent(view.context, StatsCarActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("currentCar", currentCar)
            view.context.startActivity(intent)
        }
        return currentItemView
    }
}