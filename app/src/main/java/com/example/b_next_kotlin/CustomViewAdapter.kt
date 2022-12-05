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
package com.example.bnext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.b_next_kotlin.R
import model.Feedback
import java.util.*

class CustomViewAdapter// pass the context and arrayList for the super
// constructor of the ArrayAdapter class
// invoke the suitable constructor of the ArrayAdapter class
    (context: Context, var feedbacks: ArrayList<Feedback>) :
    ArrayAdapter<Feedback?>(context, 0, feedbacks as List<Feedback?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // convertView which is recyclable view
        var currentItemView = convertView

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context).inflate(R.layout.custom_list_view, parent, false)
        }

        // get the position of the view from the ArrayAdapter
        val currentNumberPosition = getItem(position)

        // then according to the position of the view assign the desired image for the same
        val userImage = currentItemView!!.findViewById<ImageView>(R.id.imageView)
        //assert currentNumberPosition != null;
        userImage.setImageResource(R.drawable.user_comment)

        // rendo maiuscole la prima lettera del nome e la prima lettera del cognome
        val nome = (currentNumberPosition!!.user.name?.substring(0, 1)
            ?.uppercase(Locale.getDefault()) ?: "") + currentNumberPosition.user.name?.substring(1)
        val cognome = (currentNumberPosition.user.surname?.substring(0, 1)
            ?.uppercase(Locale.getDefault()) ?: "") + currentNumberPosition.user.surname?.substring(1)
        // quindi li assegno alla text view
        val textView1 = currentItemView.findViewById<TextView>(R.id.textView1)
        textView1.text = "$nome $cognome"


        // assegno il valore del commento alla seconda text view
        val textView2 = currentItemView.findViewById<TextView>(R.id.textView2)
        textView2.text = currentNumberPosition.comment

        // then return the recyclable view
        return currentItemView
    }
}