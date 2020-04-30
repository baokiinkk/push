package com.example.push

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    val bomon:MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db=FirebaseFirestore.getInstance()


        update.setOnClickListener {
            db.collection("Tổng môn").document("3").get()
                .addOnSuccessListener {
                    val x:MutableMap<String,String> = mutableMapOf()
                    x.put("","")
                    x.putAll(it.data as MutableMap<String, String>)
                    val adapter =ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, x.values.toMutableList())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerBM.adapter=adapter
                    spinnerBM.setSelection(0)
                    spinnerBM.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            BoMon.setText("")
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            BoMon.setText(x.values.elementAt(position))
                            if(x.values.elementAt(position) != "") {
                                db.collection(x.values.elementAt(position))
                                    .document(x.values.elementAt(position)).get()
                                    .addOnSuccessListener {
                                        val xx: MutableMap<String, String> = mutableMapOf()
                                        xx.put("", "")
                                        xx.putAll(it.data as MutableMap<String, String>)
                                        val adapter2 = ArrayAdapter<String>(
                                            applicationContext,
                                            android.R.layout.simple_spinner_item,
                                            xx.values.toMutableList()
                                        )
                                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                        spinnerDT.adapter = adapter2
                                        spinnerDT.setSelection(0)
                                        spinnerDT.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                                    deThi.setText("")
                                                }

                                                override fun onItemSelected(
                                                    parent: AdapterView<*>?,
                                                    view: View?,
                                                    position: Int,
                                                    id: Long
                                                ) {
                                                    deThi.setText(xx.values.elementAt(position))
                                                }

                                            }
                                    }
                            }
                        }

                    }
                }

        }

        push.setOnClickListener {
            val user = HashMap<String,String>()
            user.put("Câu hỏi",Cauhoi.text.toString())
            user.put("A",A.text.toString())
            user.put("B",B.text.toString())
            user.put("C",C.text.toString())
            user.put("D",D.text.toString())
            user.put("Đáp án",DA.text.toString())

            db.collection("Tổng môn").document("3").get()
                .addOnSuccessListener {
                    val temp = mutableMapOf<String,String>()
                    if(it.data != null) {
                        temp.putAll(it.data as MutableMap<String, String>)
                    }
                    temp.put(BoMon.text.toString(), BoMon.text.toString())
                    db.collection("Tổng môn").document("3")
                        .set(temp)
                        .addOnSuccessListener {
                            db.collection(BoMon.text.toString()).document(BoMon.text.toString()).get()
                                .addOnSuccessListener {
                                    val temp2 = mutableMapOf<String,String>()
                                    if(it.data != null)
                                        temp2.putAll(it.data as MutableMap<String, String>)
                                    temp2.put(deThi.text.toString(),deThi.text.toString())
                                    db.collection(BoMon.text.toString()).document(BoMon.text.toString())
                                        .set(temp2)
                                        .addOnSuccessListener {
                                            db.collection(BoMon.text.toString()).document(BoMon.text.toString()).collection(deThi.text.toString())
                                                .add(user)
                                                .addOnSuccessListener {
                                                    Toast.makeText(applicationContext,"Push thành công",Toast.LENGTH_SHORT).show()
                                                    A.setText("")
                                                    B.setText("")
                                                    C.setText("")
                                                    D.setText("")
                                                    DA.setText("")
                                                    Cauhoi.setText("")
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(applicationContext,"Push Thất Bại!!",Toast.LENGTH_SHORT).show()
                                                }
                                        }

                                }

                        }
                }

        }

    }

}
