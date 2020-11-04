package com.example.madtap

class Word(val name: String, val zhuyin: String, val img: Int) {

    fun show() {
        println(""""
                name: $name
                img: $img
                """)
    }
}