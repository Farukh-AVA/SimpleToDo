package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data set has been changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. Let's detect  when the user click on the button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            //Code in here is going to be executed when the user clicks on a button
//
//            System.out.println("User clicked on button");
//        }
        //listOfTasks.add("Do laundry")
        //listOfTasks.add("Go for a walk")
        loadItems()

        // Look up to recyclerView in layout
        val recycleView = findViewById<RecyclerView>(R.id.recycleView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recycleView.adapter = adapter
        // Set layout manager to position the items
        recycleView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskFeild)
        // Get a reference to the button
        // and then set onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. Grab the text the user has inputted into  @addTaskField
            val userInputtedTask = inputTextField.text.toString()
            //2. Add the string to our list of tusks: listOfTasks.
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted( listOfTasks.size-1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }
    // Save the data that user has inputted
    // By writing and reading from the file

    // Create the method to get the file we need
    fun getDataFile(): File {

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }


    // Load the items be reading every line from the data file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }



    // Save the items by writing them into out data file
    fun saveItems(){
        //FileUtils.writeLines(getDataFile(),listOfTasks)
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }



}