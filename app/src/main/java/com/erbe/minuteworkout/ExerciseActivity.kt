package com.erbe.minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.erbe.minuteworkout.Adapter.ExerciseAdapter
import com.erbe.minuteworkout.Data.Exercises
import com.erbe.minuteworkout.Model.ExerciseModel
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer : CountDownTimer? = null
    private var restProgress = 0
    private var restTime : Long = 10

    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTime : Long  = 30

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech?  = null
    private var player : MediaPlayer? = null
    private var exerciseAdapter: ExerciseAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar_exercise_activity.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        tts = TextToSpeech(this,this)

        exerciseList = Exercises.defaultExerciseList()
        setupRestView()

        setupExerciseStatusRecyclerView()
    }

    private fun setupExerciseStatusRecyclerView() {
        exercise_status_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseAdapter(exerciseList!!, this)
        exercise_status_rv.adapter = exerciseAdapter
    }

    private fun setupRestView() {

        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rest_view_ll.visibility = View.VISIBLE
        exercise_view_ll.visibility = View.GONE

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        next_exercise_tv.text = exerciseList!![currentExercisePosition + 1].getName()
        setRestProgressBar()
    }

    private fun setRestProgressBar() {

        progressBar.progress = restProgress

        restTimer = object : CountDownTimer(restTime * 1000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                progressBar.progress  = restTime.toInt() - restProgress
                timer_tv.text = (restTime - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()

    }

    private fun setupExerciseView() {

        exercise_view_ll.visibility = View.VISIBLE
        rest_view_ll.visibility = View.GONE

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        setExerciseProgressBar()

        speakOut(exerciseList!![currentExercisePosition].getName())

        image_iv.setImageResource(exerciseList!![currentExercisePosition].getImage())
        exercise_name_tv.text = exerciseList!![currentExercisePosition].getName()
    }

    private fun setExerciseProgressBar() {

        exercise_progressBar.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTime * 1000, 1000) {

            override fun onTick(p0: Long) {
                exerciseProgress++
                exercise_progressBar.progress = exerciseTime.toInt() - exerciseProgress
                exercise_timer_tv.text = (exerciseTime - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1) {

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged() //update the adapter about change the data
                    setupRestView()
                }
                else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun speakOut(name: String) {
        tts!!.speak(name, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        customDialog.yes_btn.setOnClickListener {
            finish()
            customDialog.dismiss()
        }

        customDialog.no_btn.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "The language specified is not supported", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this, "Initialization Failed", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    override fun onDestroy() {

        // rest view
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        // exercise view
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        // text to speech
        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        // media player
        if(player!=null){
            player!!.stop()
        }

        super.onDestroy()
    }

}