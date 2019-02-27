package com.apolets.dbapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BooksFragment.IDataUpdate {
    override fun onChange() {
        updateList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().add(R.id.fragmentContainer, BooksFragment()).commit()

        updateList()

        swtch.setOnCheckedChangeListener(fun(buttonView: CompoundButton?, isChecked: Boolean) {

            if (!isChecked) fragmentManager.beginTransaction().replace(R.id.fragmentContainer, BooksFragment()).commit()
            else fragmentManager.beginTransaction().replace(R.id.fragmentContainer, BookDetailsFragment()).commit()
        })


    }

    fun updateList() {
        AsyncTask.execute {
            kotlin.run {
                val books = LibDB.get(this).bookDao().getAllBooksWithDetails()

                runOnUiThread { listBooks.adapter = ArrayAdapter<BooksWithDetails>(this, android.R.layout.simple_list_item_1, books) }
            }
        }
    }
}
