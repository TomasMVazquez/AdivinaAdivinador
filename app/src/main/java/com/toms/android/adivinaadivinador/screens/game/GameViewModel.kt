package com.toms.android.adivinaadivinador.screens.game

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.toms.android.adivinaadivinador.R

//the different buzz pattern Long array constants here
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel(application: Application,list: String): AndroidViewModel(application){

    // These are the three different types of buzzing in the game. Buzz pattern is the number of
    // milliseconds each interval of buzzing and non-buzzing takes.
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    // The current word
    private var _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private lateinit var animals: MutableList<String>
    private lateinit var places: MutableList<String>
    private lateinit var stuff: MutableList<String>

    private var _listOf = String()

    //Game Finish Event
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = _eventGameFinish

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    //Timer
    private val timer : CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    init {
        var string = getSomeString(R.string.animals_list)
        if (list != "") {
            _listOf = list
        }
        _eventGameFinish.value = false
        resetList(_listOf)
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList(listOf: String) {
        animals = mutableListOf(
                "vaca",
                "perro",
                "abeja",
                "águila",
                "araña",
                "avispa",
                "ballena",
                "bisonte",
                "búfalo",
                "burro",
                "caballo",
                "camello",
                "canario",
                "cangrejo",
                "canguro",
                "caracol",
                "cebra",
                "cerdo",
                "chimpancé",
                "ciervo",
                "cisne",
                "cocodrilo",
                "elefante",
                "escarabajo",
                "escorpión",
                "foca",
                "gallina",
                "gallo",
                "gato",
                "golondrina",
                "hipopótamo",
                "hormiga",
                "jabalí",
                "jirafa",
                "león",
                "loro",
                "mosca",
                "mosquito",
                "oso",
                "oveja",
                "perdiz",
                "perro",
                "pingüino",
                "pollo",
                "saltamontes",
                "serpiente",
                "tigre",
                "topo",
                "toro",
                "tortuga",
                "vaca",
                "zorro"
        )
        places = mutableListOf(
                "paris",
                "madrid",
                "buenos aires",
                "nueva york",
                "tokio",
                "egipto",
                "australia",
                "barcelona",
                "moscú"
        )
        stuff = mutableListOf(
                "tijera",
                "peine",
                "cepillo",
                "escoba",
                "silla",
                "mesa",
                "coche",
                "sirena",
                "cesto",
                "sillón",
                "televisor",
                "radio",
                "robot",
                "celular"
        )

        wordList = when (listOf) {
            getSomeString(R.string.animals_list) -> animals
            getSomeString(R.string.places_list) -> places
            getSomeString(R.string.stuff_list) -> stuff
            else -> (animals + places + stuff).toMutableList()
        }

        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList(_listOf)
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    //GameFinish func aware of changeing states
    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

    fun getSomeString(id: Int): String? {
        return getApplication<Application>().resources.getString(id)
    }

    companion object {
        private const val TAG = "TMV:"
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L

    }
}