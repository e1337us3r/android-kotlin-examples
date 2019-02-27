package com.apolets.dbapp

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_book_details.*


class BookDetailsFragment : Fragment() {
    //private var listener: OnFragmentInteractionListener? = null
    lateinit var changeCallback: BooksFragment.IDataUpdate

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false)

    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            changeCallback = activity as BooksFragment.IDataUpdate
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement IDataUpdate")
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btnAddCopy.setOnClickListener {
            run {
                AsyncTask.execute {
                    val db = LibDB.get(context)
                    db.bookDetailsDao().insert(BookDetail(book = inputBookId.text.toString().toInt(), library = inputLibrary.text.toString(), code = inputCode.text.toString()))
                    changeCallback.onChange()
                }

            }
        }


    }


}