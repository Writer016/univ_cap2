package com.univ.univ_walk_main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var menuButton: FloatingActionButton
    lateinit var achievementButton: FloatingActionButton
    lateinit var graphButton: FloatingActionButton
    lateinit var friendButton: FloatingActionButton

    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation

    private var isMenuClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuButton = findViewById(R.id.button_menu)
        achievementButton = findViewById(R.id.button_achievement)
        graphButton = findViewById(R.id.button_graph)
        friendButton = findViewById(R.id.button_friend)

        outAnim = AnimationUtils.loadAnimation(this, R.anim.fitem_out_anim)
        inAnim = AnimationUtils.loadAnimation(this, R.anim.fitem_in_anim)

        menuButton.setOnClickListener {
            floatMenuClicked()
        }
    }

    private fun floatMenuClicked(){
        isMenuClicked = !isMenuClicked
        setVisibility()
        setAnimation()
    }

    private fun setVisibility(){
        if(isMenuClicked){
            friendButton.visibility = View.VISIBLE
            graphButton.visibility = View.VISIBLE
            achievementButton.visibility = View.VISIBLE
        }else{
            friendButton.visibility = View.INVISIBLE
            graphButton.visibility = View.INVISIBLE
            achievementButton.visibility = View.INVISIBLE
        }
    }


    private fun setAnimation(){
        if(isMenuClicked){
            friendButton.startAnimation(outAnim)
            graphButton.startAnimation(outAnim)
            achievementButton.startAnimation(outAnim)
        } else {
            friendButton.startAnimation(inAnim)
            graphButton.startAnimation(inAnim)
            achievementButton.startAnimation(inAnim)
        }
    }

}