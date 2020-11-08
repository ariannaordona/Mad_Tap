package com.example.madtap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.TransitionManager.beginDelayedTransition
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_grid.*
import kotlinx.android.synthetic.main.image_grid.*
import kotlinx.android.synthetic.main.middle_bar.*
import kotlinx.android.synthetic.main.top_bar.*
import java.util.*
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    //TODO return from database will give a list of objects
    // Will create lists based on the returned images, for now it is hard-coded
    private val vocab = arrayOf("蘋果", "香蕉", "檸檬", "橘子")

    private val apple = Word("蘋果", "ㄆㄧㄥˊ ㄍㄨㄛˇ", R.drawable.apple)
    private val banana = Word("香蕉", "ㄒㄧㄤ ㄐㄧㄠ", R.drawable.banana)
    private val lemon = Word("檸檬", "ㄋㄧㄥˊ ㄇㄥˊ", R.drawable.lemon)
    private val orange = Word("橘子", "ㄐㄩˊ ㄗˇ",R.drawable.orange)

    private val vocabWord = arrayOf(apple, banana, lemon, orange)

    private val r = Random()
    lateinit var answerObject: Word
    lateinit var answer: String
    var answerArray = ArrayList<Word>()
    var currentScore = 0
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()
        initGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.game_options_menu, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun initGame() {
        //Initialize all views
        //Populate answer array with random words then set images based on array list
        currentScore = 0
        answerObject = vocabWord[r.nextInt(vocabWord.size)]
        answer = answerObject.name
        answerArray.add(answerObject)
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])

        tv_zhuyin.text = answerObject.zhuyin
        tv_answerNumber.text = answer.length.toString()
        tv_answerHint.text = makeVertical(answerObject.name)

        sharedPref = getSharedPreferences("PREFS", MODE_PRIVATE)
        tv_best.text = "BEST: ${sharedPref.getInt("bestScore", 0)}"

        iv_1.setImageResource(answerObject.img)
        iv_2.setImageResource(answerArray[1].img)
        iv_3.setImageResource(answerArray[2].img)
        iv_4.setImageResource(answerArray[3].img)

        setButtonText()
        startTimer()
    }

    @SuppressLint("SetTextI18n")
    private fun correctAnswer() {
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

        currentScore++
        tv_score.text = "SCORE: $currentScore"

        //TODO add a green highlight flash to show user that it is correct or play a sound

        //TODO HARD Mode (enabled toggle in menu) Shuffle the button text every time they answer
    }

    private fun incorrectAnswer() {
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

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun makeVertical(v: String): String {
        val sb = StringBuilder()
        for(i in v.indices) {
            sb.append(v[i])
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun imageAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide)
        iv_1.startAnimation(animation)
        iv_2.startAnimation(animation)
        iv_3.startAnimation(animation)
        iv_4.startAnimation(animation)
    }

    private fun setListeners() {
        val listener = View.OnClickListener { v ->
            val b = v as Button
            tv_answer.append(b.text)
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
        //textWatcher checks et_answer whenever the text is changed
        // if the length of user input is the same length as the answer it will check if the user input is correct
        // when answer is correct will call function correctAnswer and incorrectAnswer if incorrect
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val v = p0.toString()
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

    private fun setButtonText() {
        //populates buttons with vocab chars based on a shuffled array of 8
        val buttons = arrayOf(0, 1, 2, 3, 4, 5, 6 , 7)
        buttons.shuffle()
        //StringBuilder is used to combine all vocab characters into one variable v
        //Then v is used to populate all the buttons with a single different character
        val sb = StringBuilder()
        for (i in vocab) { sb.append(i) }
        val v = sb.toString()
        b_1.text = v[buttons[0]].toString()
        b_2.text = v[buttons[1]].toString()
        b_3.text = v[buttons[2]].toString()
        b_4.text = v[buttons[3]].toString()
        b_5.text = v[buttons[4]].toString()
        b_6.text = v[buttons[5]].toString()
        b_7.text = v[buttons[6]].toString()
        b_8.text = v[buttons[7]].toString()
    }

    private fun startTimer() {
        var timer = object : CountDownTimer(50000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(milliUntilFinished: Long) {
                tv_time.text = "TIME: ${MILLISECONDS.toSeconds(milliUntilFinished)}"
            }
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                tv_answerHint.text = ""
                tv_answerNumber.text = ""
                tv_answer.text = ""
                tv_zhuyin.text =""
                iv_1.setImageResource(android.R.color.transparent)
                iv_2.setImageResource(android.R.color.transparent)
                iv_3.setImageResource(android.R.color.transparent)
                iv_4.setImageResource(android.R.color.transparent)
                b_1.text = ""
                b_2.text = ""
                b_3.text = ""
                b_4.text = ""
                b_5.text = ""
                b_6.text = ""
                b_7.text = ""
                b_8.text = ""

                //TODO toast does not stand out
                Toast.makeText(this@MainActivity, "Game Over!", Toast.LENGTH_SHORT).show()

                //TODO reset the game and add a play again option
                var bestScore = sharedPref.getInt("bestScore", 0)
                if(currentScore > bestScore) {
                    bestScore = currentScore
                    tv_best.text = "BEST: $bestScore"

                    val sharedPref: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putInt("bestScore", bestScore)
                    editor.apply()
                }
                //Creating popup window
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val popupView = inflater.inflate(R.layout.popup_window,null)
                val popupWindow = PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                //Set an elevation for the popup window
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.elevation = 10.0F
                }
                //If API level 23 or higher then execute the code
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Create a new slide animation for popup window enter transition
                    val slideIn = Slide()
                    slideIn.slideEdge = Gravity.TOP
                    popupWindow.enterTransition = slideIn

                    //Slide animation for popup window exit transition
                    val slideOut = Slide()
                    slideOut.slideEdge = Gravity.RIGHT
                    popupWindow.exitTransition = slideOut
                }
                val popupText = popupView.findViewById<TextView>(R.id.tv_gameOver)
                popupText.text = "GAME OVER!\nYour Score: $currentScore\nHigh Score: $bestScore"
                val popupPlay = popupView.findViewById<Button>(R.id.b_popup_play)
                val popupQuit = popupView.findViewById<Button>(R.id.b_popup_quit)
                popupPlay.setOnClickListener {
                    initGame()
                    popupWindow.dismiss()
                }
                popupQuit.setOnClickListener {
                    //TODO quit to main menu
                }
                popupWindow.setOnDismissListener {
                    //TODO replace with a countdown to the game
                    Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
                }

                beginDelayedTransition(game_layout)
                popupWindow.showAtLocation(
                    game_layout,
                    Gravity.CENTER,
                    0,
                    0
                )
            }
        }.start()
    }

    //TODO create pause functionality, need to research timer pause
}