package com.kyledahlin.myrulebot.rules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RuleClassViewModel @Inject constructor(private val _repo: RuleRepo) : ViewModel() {

    private val _rules = MutableLiveData<List<RuleEntry>>()

    val ruleEntries: LiveData<List<RuleEntry>> = _rules

    fun refreshRules() {
        val entries = _repo.getRuleEntries()
        _rules.postValue(entries)
    }
}