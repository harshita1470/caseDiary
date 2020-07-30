package com.example.casediary

import android.net.Uri
import io.realm.RealmList
import io.realm.RealmObject


open class CaseStructure (
    var id:String="",
    var name:String="",
    var year:String="",
    var plantiff:String="",
    var defendant:String="",
    var type:String="*Type of Case",
    var desc:String="*Other details if required",
   var hearings:RealmList<HearingStructure> = RealmList()
 ):RealmObject()

open class HearingStructure (
    var date:String="",
    var order:String="",
    var applicno:String="",
    var listedon:String="",
    var files:RealmList<FileStructure> = RealmList()
):RealmObject()

open class FileStructure (
    var file:String="",
    var type:String?=""
):RealmObject()