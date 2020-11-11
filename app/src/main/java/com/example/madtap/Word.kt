package com.example.madtap

class Word(val name: String, val zhuyin: String, val pinyin: String, val img: Int) {

    fun show() {
        println(""""
                name: $name
                zhuyin: $zhuyin
                pinyin: $pinyin
                img: $img
                """)
    }
}