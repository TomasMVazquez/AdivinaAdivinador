package com.toms.android.adivinaadivinador.screens.game

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.*
import com.toms.android.adivinaadivinador.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//the different buzz pattern Long array constants here
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel(
        val database: ListDatabaseDao,
        application: Application,
        list: String): AndroidViewModel(application){

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

    private val allWords = database.getAllWords()
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private var created: MutableList<String> = mutableListOf()

    private var _listOf = String()
    val listOf : String
        get() = _listOf

    //Game Finish Event
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = _eventGameFinish

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    //Timer
    private lateinit var timer : CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    //Coroutines
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //Event to show ImageView
    private val _eventCallApi = MutableLiveData<Boolean>()
    val eventCallApi: LiveData<Boolean>
        get() = _eventCallApi

    private val _eventTimerApi = MutableLiveData<Boolean>()
    val eventTimerApi: LiveData<Boolean>
        get() = _eventTimerApi

    //Status de la llamada
    enum class ApiStatus { LOADING, ERROR, DONE }
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    //Objeto Imagen de la Api
    private val _imageFromApi = MutableLiveData<ApiImage>()
    val imageFromApi: LiveData<ApiImage>
        get() = _imageFromApi

    init {
        getCreatedListfromDataBase()
        if (list != "") {
            _listOf = list
            if (list.equals(getSomeString(R.string.anmals_picture_list))){
                onCallApi()
            }
        }
        _eventGameFinish.value = false
        resetList(_listOf)
        nextWord()
        _score.value = 0

        startMyTimer(COUNTDOWN_TIME)
    }

    private fun startMyTimer(startingTime:Long){
        timer = object : CountDownTimer(startingTime, ONE_SECOND) {

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

    private fun stopMyTimer(){
        timer.cancel()
    }

    private fun resumeMyTimer(){
        if (currentTime.value == null) {
            startMyTimer(COUNTDOWN_TIME)
            onTimerStopOnce()
        }
    }

    private fun getCreatedListfromDataBase() {
        allWords.forEach {
            created.add(it.itemWord.toString())
        }
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList(listOf: String) {

        wordList = when (listOf) {
            getSomeString(R.string.animals_list) -> listOfAnimals.toMutableList()
            getSomeString(R.string.places_list) -> listOfCities.toMutableList()
            getSomeString(R.string.stuff_list) -> listOfStuffs.toMutableList()
            getSomeString(R.string.anmals_picture_list) -> listOfAnimalsForKids.toMutableList()
            getSomeString(R.string.created_list) -> created.toMutableList()
            else -> (listOfAnimals + listOfCities + listOfStuffs + created).toMutableList()
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
        _word.value = wordList.removeAt(0).toUpperCase()
        if (_listOf.equals(getSomeString(R.string.anmals_picture_list))){
            getAnimalImagesFromAPI(word.value.toString())
            _imageFromApi.value
        }
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

    fun onCallApi(){
        _eventCallApi.value = true
        _eventTimerApi.value = true
    }

    fun onTimerStopOnce(){
        _eventTimerApi.value = false
    }

    fun onEndedCallApi(){
        _eventCallApi.value = false
    }

    fun getSomeString(id: Int): String? {
        return getApplication<Application>().resources.getString(id)
    }

    //Get Images from API
    private fun getAnimalImagesFromAPI(animalToSearch: String){
        coroutineScope.launch {
            var getAnimals = ImageApi.retrofitService.getAnimals(QUERY_KEY,animalToSearch, QUERY_LANG_ES, QUERY_IMAGE_TYPE_PHOTO, QUERY_ORIENTATION_H, QUERY_CATEGORY_ANIMALS, QUERY_COLORS_ALL)
            try {
                _status.value = ApiStatus.LOADING
                if (eventTimerApi.value!!) stopMyTimer()
                var result = getAnimals.await()
                if (eventTimerApi.value!!) resumeMyTimer()
                _status.value = ApiStatus.DONE
                if (result.hits.size > 0) {
                    _imageFromApi.value = result.hits[0]
                }
            }catch (t:Throwable){
                _status.value = ApiStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        viewModelJob.cancel()
        onEndedCallApi()
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