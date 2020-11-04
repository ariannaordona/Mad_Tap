package com.example.madtap

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.grid.*
import kotlinx.android.synthetic.main.middle_bar.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    //TODO return from database will give a list of objects
    // Will create lists based on the returned images
    private val vocab = arrayOf("蘋果", "香蕉", "檸檬", "橘子")

    private val apple = Word("蘋果", "ㄆㄧㄥˊ ㄍㄨㄛˇ", R.drawable.apple)
    private val banana = Word("香蕉", "ㄒㄧㄤ ㄐㄧㄠ", R.drawable.banana)
    private val lemon = Word("檸檬", "ㄋㄧㄥˊ ㄇㄥˊ", R.drawable.lemon)
    private val orange = Word("橘子", "ㄐㄩˊ ㄗˇ",R.drawable.orange)

    private val vocabWord = arrayOf(apple, banana, lemon, orange)

    val r = Random()
    var answerObject = vocabWord[r.nextInt(vocabWord.size)]
    var answer = answerObject.name
    var answerArray = ArrayList<Word>()

    //TODO make the imageviews visually slide down like rolling

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Populate answer array with random words then set images based on array list
        answerArray.add(answerObject)
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])

        iv_1.setImageResource(answerObject.img)
        iv_2.setImageResource(answerArray[1].img)
        iv_3.setImageResource(answerArray[2].img)
        iv_4.setImageResource(answerArray[3].img)

        tv_zhuyin.text = answerObject.zhuyin
        tv_answerNumber.text = answer.length.toString()
        tv_answerHint.text = makeVertical(answerObject.name)


        //Setting all input buttons to listeners
        val listener = View.OnClickListener { v ->
            val b = v as Button
            tv_answer.append(b.text)
 //           et_answer.append("\n")
        }

        b_1.setOnClickListener(listener)
        b_2.setOnClickListener(listener)
        b_3.setOnClickListener(listener)
        b_4.setOnClickListener(listener)
        b_5.setOnClickListener(listener)
        b_6.setOnClickListener(listener)
        b_7.setOnClickListener(listener)
        b_8.setOnClickListener(listener)

        b_clear.setOnClickListener {
            tv_answer.text = ""
        }

        //populates buttons with vocab chars based on a shuffled array of 8
        val buttons = arrayOf(0, 1, 2, 3, 4, 5, 6 , 7)
        buttons.shuffle()
        //v is all the vocab chars in one string and is used to populates buttons with 1 char
        val sb = StringBuilder()
        for (i in vocab) {
            sb.append(i)
        }
        val v = sb.toString()
        b_1.text = v[buttons[0]].toString()
        b_2.text = v[buttons[1]].toString()
        b_3.text = v[buttons[2]].toString()
        b_4.text = v[buttons[3]].toString()
        b_5.text = v[buttons[4]].toString()
        b_6.text = v[buttons[5]].toString()
        b_7.text = v[buttons[6]].toString()
        b_8.text = v[buttons[7]].toString()

        //checks et_answer whenever the text is changed
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
               val v = p0.toString()
                // if the length of user input is the same length as the answer, will check if the user input is correct
                // when it is correct, will call function correctAnswer to update game, if incorrect will clear et_answer
                if (v.length == answer.length) {
                    if (v == answer) {correctAnswer()}
                    else {
                        tv_answer.text = ""
                        incorrectAnswer()
                    }
                }
            }
        }
        tv_answer.addTextChangedListener(textWatcher)
    }

    private fun correctAnswer() {
        Log.d(TAG, "correctAnswer: called")

        imageAnimation()
        tv_answer.text = ""
        answerArray.removeAt(0)
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])

        answerObject = answerArray[0]
        answer = answerArray[0].name
        tv_answerNumber.text = answer.length.toString()
        tv_zhuyin.text = answerObject.zhuyin
        tv_answerHint.text = makeVertical(answerObject.name)
        iv_1.setImageResource(answerArray[0].img)
        iv_2.setImageResource(answerArray[1].img)
        iv_3.setImageResource(answerArray[2].img)
        iv_4.setImageResource(answerArray[3].img)

        //TODO add a green highlight flash to show user that it is correct or play a sound

        //TODO HARD Mode (enabled toggle in menu) Shuffle the button text every time they answer
    }

    private fun incorrectAnswer() {
        Log.d(TAG, "incorrectAnswer: called")
        tv_answer.text = ""
        //TODO add a red highlight flash or a red X on the image to give user feedback that their answer was not correct
    }

    override fun onResume() {
        Log.d(TAG, "onResume: called")
        super.onResume()
        //TODO if the player is in the middle of a round
        // have the pop-up notification card pop-up
        // player must hit the resume button to actually continue the game
        // else, game is already finished and on a play again screen
    }

    override fun onPause() {
        Log.d(TAG, "onPause: called")
        super.onPause()
        //TODO if player is in the middle of a round must pause time
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d(TAG, "onSaveInstanceState: called")
        super.onSaveInstanceState(outState, outPersistentState)
        // TODO potentially needs to save et_answer value
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState: called")
        super.onRestoreInstanceState(savedInstanceState)
        //TODO potentially needs to restore et_answer value
    }

    private fun makeVertical(v: String): String {
        Log.d(TAG, "makeVertical: called")
        val sb = StringBuilder()
        for(i in v.indices) {
            sb.append(v[i])
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun imageAnimation() {
        Log.d(TAG, "imageAnimation: called")

        val animation = AnimationUtils.loadAnimation(this, R.anim.slide)
        iv_1.startAnimation(animation)
        iv_2.startAnimation(animation)
        iv_3.startAnimation(animation)
        iv_4.startAnimation(animation)
    }

}