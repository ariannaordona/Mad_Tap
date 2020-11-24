package com.example.madtap

import android.content.Context
import android.content.Intent
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
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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

    private val apple = Word("蘋果", "ㄆㄧㄥˊ ㄍㄨㄛˇ", "píng guǒ", R.drawable.apple)
    private val banana = Word("香蕉", "ㄒㄧㄤ ㄐㄧㄠ", "xiāng jiāo", R.drawable.banana)
    private val lemon = Word("檸檬", "ㄋㄧㄥˊ ㄇㄥˊ", "níng méng", R.drawable.lemon)
    private val orange = Word("橘子", "ㄐㄩˊ ㄗˇ", "jú zǐ", R.drawable.orange)

    private val vocabWord = arrayOf(apple, banana, lemon, orange)

    private val r = Random()
    lateinit var timer: CountDownTimer
    lateinit var answerObject: Word
    lateinit var answer: String
    var answerArray = ArrayList<Word>()
    var currentScore = 0
    private lateinit var sharedPref: SharedPreferences
    lateinit var gameMode: String

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSettings()
        initGame()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun initGame() {
        //Initialize all views
        //Populate answer array with random words then set images based on array
        currentScore = 0
        answerObject = vocabWord[r.nextInt(vocabWord.size)]
        answer = answerObject.name
        answerArray.add(answerObject)
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])

        tv_zhuyin.text = answerObject.zhuyin
        tv_pinyin.text = answerObject.pinyin
        tv_answerNumber.text = answer.length.toString()
        tv_answerHint.text = makeVertical(answerObject.name)

        iv_1.setImageResource(answerObject.img)
        iv_2.setImageResource(answerArray[1].img)
        iv_3.setImageResource(answerArray[2].img)
        iv_4.setImageResource(answerArray[3].img)

        tv_time.text = "TIME: 50"
        tv_answer.text = ""
        setButtonText()
        if (gameMode == "standard") {
            sharedPref = getSharedPreferences("PREFS", MODE_PRIVATE)
            tv_best.text = "BEST: ${sharedPref.getInt("bestScore", 0)}"
            iv_pause.setImageResource(R.drawable.pause_button)
            startTimer(54000)
        } else {
            sharedPref = getSharedPreferences("PREFS_END", MODE_PRIVATE)
            tv_best.text = "BEST: ${sharedPref.getInt("endlessBestScore", 0)}"
        }
    }

    private fun setSettings() {
        val intent = intent
        val extras = intent.extras

        if (extras != null) {
            Log.d(TAG, "TRADTIONAL Key = ${extras.getBoolean("TRADITIONAL")}")
            Log.d(TAG, "CATEGORY Key = ${extras.getString("CATEGORY")}")
            if (extras.getBoolean("ZHUYIN")) {
                tv_zhuyin.visibility = View.VISIBLE
            } else {tv_zhuyin.visibility = View.INVISIBLE}
            if (extras.getBoolean("PINYIN")) {
                tv_pinyin.visibility = View.VISIBLE
            } else {tv_pinyin.visibility = View.INVISIBLE}

            when (extras.getString("MODE")) {
                "Practice" -> {
                    include1.visibility = View.INVISIBLE
                    gameMode = "practice"
                }
                "Endless" -> {
                    gameMode = "endless"
                    tv_time.text = "TIME: ∞"
                }
                else -> { gameMode = "standard" }
            }
        }
    }

    private fun correctAnswer() {
        Log.d(TAG, "correctAnswer called")
        imageAnimation()
        tv_answer.text = ""
        answerArray.removeAt(0)
        answerArray.add(vocabWord[r.nextInt(vocabWord.size)])

        answerObject = answerArray[0]
        answer = answerArray[0].name
        tv_answerNumber.text = answer.length.toString()
        tv_zhuyin.text = answerObject.zhuyin
        tv_pinyin.text = answerObject.pinyin
        tv_answerHint.text = makeVertical(answerObject.name)
        currentScore++
        tv_score.text = "SCORE: $currentScore"

        //TODO add a green highlight flash to show user that it is correct or play a sound

        //TODO HARD Mode (enabled toggle in menu) Shuffle the button text every time they answer
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun incorrectAnswer() {
        Log.d(TAG, "incorrectAnswer called")
        val animation = AnimationUtils.loadAnimation(this, R.anim.incorrect)
        iv_1.startAnimation(animation)
        tv_answer.text = ""
        if (gameMode == "endless") {
            clearViews()
            setBestScore(sharedPref.getInt("endlessBestScore", 0))
            createEndPopUpWindow(sharedPref.getInt("endlessBestScore", 0))
        }
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
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState: called")
        super.onRestoreInstanceState(savedInstanceState)
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
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.slide)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide)
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.slide)
        val animation4 = AnimationUtils.loadAnimation(this, R.anim.slide)
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) { iv_1.setImageResource(answerArray[0].img) }
        })
        animation2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) { iv_2.setImageResource(answerArray[1].img) }
        })
        animation3.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) { iv_3.setImageResource(answerArray[2].img) }
        })
        animation4.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) { iv_4.setImageResource(answerArray[3].img) }
        })
        iv_1.startAnimation(animation1)
        iv_2.startAnimation(animation2)
        iv_3.startAnimation(animation3)
        iv_4.startAnimation(animation4)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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
        //When user answer length is equal to answer length call correctAnswer or incorrectAnswer accordingly
        val textWatcher = object : TextWatcher {
            var isOnTextChanged = false
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { isOnTextChanged = true}
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun afterTextChanged(p0: Editable?) {
                if (isOnTextChanged) {
                    isOnTextChanged = false
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
        }
        tv_answer.addTextChangedListener(textWatcher)

        if(gameMode == "standard") {
            iv_pause.setOnClickListener {
                pause()
            }
        }
    }

    private fun disableListeners() {
        b_1.setOnClickListener(null)
        b_2.setOnClickListener(null)
        b_3.setOnClickListener(null)
        b_4.setOnClickListener(null)
        b_5.setOnClickListener(null)
        b_6.setOnClickListener(null)
        b_7.setOnClickListener(null)
        b_8.setOnClickListener(null)
        b_clear.setOnClickListener(null)
        iv_pause.setOnClickListener(null)
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

    private fun startTimer(t: Long) {
        var start = true
        timer = object : CountDownTimer(t, 1000) {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onTick(milliUntilFinished: Long) {
                val timeSeconds = MILLISECONDS.toSeconds(milliUntilFinished).toInt()
                if(start) {
                    when {
                        timeSeconds >= 53 -> {countdown(3)}
                        timeSeconds in 52..52 -> {countdown(2)}
                        timeSeconds in 51..51 -> { countdown(1) }
                        timeSeconds in 50..50 -> {
                            setListeners()
                            start = false
                        }
                        else -> {start = false}
                    }
                } else {tv_time.text = "TIME: $timeSeconds"}
            }
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onFinish() {
                clearViews()
                setBestScore(sharedPref.getInt("bestScore", 0))
                createEndPopUpWindow(sharedPref.getInt("bestScore", 0))
            }
        }.start()
    }

    private fun clearViews() {
        tv_answerHint.text = ""
        tv_answerNumber.text = ""
        tv_answer.text = ""
        tv_zhuyin.text =""
        tv_pinyin.text =""
        tv_score.text = "SCORE: 0"
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
    }

    private fun setBestScore(best: Int) {
        if(currentScore > best) {
            tv_best.text = "BEST: $currentScore"
            if (gameMode == "standard") {
                val sharedPref: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("bestScore", currentScore)
                editor.apply()
            } else {
                val sharedPref: SharedPreferences = getSharedPreferences("PREFS_END", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("endlessBestScore", currentScore)
                editor.apply()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createEndPopUpWindow(best: Int) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_window_end,null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        disableListeners()
        //Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { popupWindow.elevation = 10.0F }
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
        popupText.text = "GAME OVER!\nYour Score: $currentScore\nHigh Score: $best"
        val popupPlay = popupView.findViewById<Button>(R.id.b_popup_play)
        val popupQuit = popupView.findViewById<Button>(R.id.b_popup_end_quit)
        popupPlay.setOnClickListener {
            clearArray()
            initGame()
            popupWindow.dismiss()
        }
        popupQuit.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
        beginDelayedTransition(game_layout)
        popupWindow.showAtLocation(game_layout, Gravity.CENTER, 0, 0)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createPausePopUpWindow(timeLeft: Long) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_window_pause,null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        //Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { popupWindow.elevation = 10.0F }
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
        val popupContinue = popupView.findViewById<Button>(R.id.b_popup_continue)
        val popupRestart = popupView.findViewById<Button>(R.id.b_popup_restart)
        val popupQuit = popupView.findViewById<Button>(R.id.b_popup_pause_quit)
        val popupZhuyin = popupView.findViewById<SwitchCompat>(R.id.b_popup_zhuyin)
        val popupPinyin = popupView.findViewById<SwitchCompat>(R.id.b_popup_pinyin)
        if (tv_zhuyin.visibility == View.VISIBLE) {popupZhuyin.isChecked = true}
        if (tv_pinyin.visibility == View.VISIBLE) {popupPinyin.isChecked = true}
        popupContinue.setOnClickListener {
            if (popupZhuyin.isChecked) {
                tv_zhuyin.visibility = View.VISIBLE
            } else {tv_zhuyin.visibility = View.INVISIBLE}
            if (popupPinyin.isChecked) {
                tv_pinyin.visibility = View.VISIBLE
            } else {tv_pinyin.visibility = View.INVISIBLE}
            popupWindow.dismiss()
            setListeners()
            startTimer(timeLeft)
        }
        popupRestart.setOnClickListener {
            if (popupZhuyin.isChecked) {
                tv_zhuyin.visibility = View.VISIBLE
            } else {tv_zhuyin.visibility = View.INVISIBLE}
            if (popupPinyin.isChecked) {
                tv_pinyin.visibility = View.VISIBLE
            } else {tv_pinyin.visibility = View.INVISIBLE}
            clearArray()
            initGame()
            popupWindow.dismiss()
        }
        popupQuit.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
        beginDelayedTransition(game_layout)
        popupWindow.showAtLocation(game_layout, Gravity.CENTER, 0, 0)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun pause() {
        var timeLeftChar = tv_time.text.takeLast(2).toString()
        timeLeftChar = timeLeftChar.replace("\\s".toRegex(), "")
        val timeLeft = (Integer.parseInt(timeLeftChar) * 1000)
        timer.cancel()
        disableListeners()
        createPausePopUpWindow(timeLeft.toLong())
    }

    private fun clearArray() {
        answerArray.removeAt(0)
        answerArray.removeAt(0)
        answerArray.removeAt(0)
        answerArray.removeAt(0)
    }

    private fun countdown(imageBlock: Int) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.game_countdown,null)
        val popup = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val imageCountdown = view.findViewById<ImageView>(R.id.iv_countdown)
        when (imageBlock) {
            3 -> imageCountdown.setImageResource(R.drawable.number_block_3)
            2 -> imageCountdown.setImageResource(R.drawable.number_block_2)
            1 -> imageCountdown.setImageResource(R.drawable.number_block_1)
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) { popup.dismiss() }
        })
        popup.showAtLocation(game_layout, Gravity.CENTER, 0, 0)
        imageCountdown.startAnimation(animation)
    }
}