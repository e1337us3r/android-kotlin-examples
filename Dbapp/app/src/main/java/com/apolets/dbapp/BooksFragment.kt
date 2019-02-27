package com.apolets.dbapp


import android.os.Bundle
import android.app.Fragment
//try support.v4.app.Fragment
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_books.*
import android.app.Activity




class BooksFragment : Fragment() {
    //private var listener: OnFragmentInteractionListener? = null
    lateinit var changeCallback: IDataUpdate

    // Container Activity must implement this interface
    interface IDataUpdate {
        fun onChange()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            changeCallback = activity as IDataUpdate
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement IDataUpdate")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnCreateBook.setOnClickListener { view ->
            run {
                AsyncTask.execute {
                    val db = LibDB.get(context)
                    db.bookDao().insert(Book(uid = 0, name = inputName.text.toString(), description = inputDesc.text.toString()))
                    changeCallback.onChange()
                }

            }
        }


    }


}
