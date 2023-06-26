package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_CUP_CAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> get() = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> get() = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String>
        get() = _price.map {
            NumberFormat.getCurrencyInstance().format(it)
        }

    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = ""
        _price.value = 0.00
    }

    init {
        resetOrder()
    }

    fun setQuantity(numberOfCupcakes: Int) {
        _quantity.value = numberOfCupcakes
        updatePrice()
    }

    fun hasNoFlavourSet(): Boolean = _flavor.value.isNullOrEmpty()

    fun setFlavour(desiredFlavour: String) {
        _flavor.value = desiredFlavour
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    val dateOptions = getPickupOptions()

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    private fun updatePrice() {
        _price.value = (_quantity.value ?: 0) * PRICE_PER_CUP_CAKE
        if (dateOptions[0] == _date.value)
            _price.value = _price.value?.plus(PRICE_FOR_SAME_DAY_PICKUP)
    }
}