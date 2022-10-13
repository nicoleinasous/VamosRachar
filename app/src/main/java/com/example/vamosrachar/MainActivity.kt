package com.example.vamosrachar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity(){

    lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instâncias
        val inputValue: EditText  = findViewById(R.id.inputValue)
        val inputPeople: EditText = findViewById(R.id.inputPeople)
        val textTotal: TextView   = findViewById(R.id.textTotal)
        val btnFloatSpeak: FloatingActionButton = findViewById(R.id.btnfloatSpeak)
        val btnFloatShare: FloatingActionButton = findViewById(R.id.btnfloatShare)

        // Função que realiza o cálculo e retorna na tela
        fun dividerResult(value: Double?, people: Int?){
            var result: Double? = null

            if (value != null && people != null && people != 0) {
                result = (value / people)
            } else {
                result = 0.0
            }

            val formatter: NumberFormat = DecimalFormat("0.##")
            textTotal.text = formatter.format(result)
        }

        // Função acionada que recalcula de acordo com o campo de valor
        inputValue.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                var value  = inputValue.text.toString().toDoubleOrNull()
                var people = inputPeople.text.toString().toIntOrNull()
                dividerResult(value,people)
            }
        })

        // Função acionada que recalcula de acordo com o campo de quantidade de pessoas
        inputPeople.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}
            override fun afterTextChanged(p0: Editable?) {
                var value  = inputValue.text.toString().toDoubleOrNull()
                var people = inputPeople.text.toString().toIntOrNull()
                dividerResult(value,people)
            }
        })

        // Configurações para a fala de texto
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                tts.setLanguage(Locale("pt","BR"))
            }
        })

        // Função aciona a fala do resultado
        btnFloatSpeak.setOnClickListener{
            val textSpeak = textTotal.text.toString()
            if(textSpeak != null){
                tts.speak(textSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

        // Função que aciona o compartilhamento do resultado por texto
        btnFloatShare.setOnClickListener{
            val textShare = "Vamos Rachar! O valor para cada um é de: R$ " + textTotal.text.toString() + ". Valor total: R$ " + inputValue.text.toString() + " para " + inputPeople.text.toString() + " pessoas."
            val intent    = Intent()

            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, textShare)
            startActivity(Intent.createChooser(intent,"Compartilhar"))
        }
    }



}