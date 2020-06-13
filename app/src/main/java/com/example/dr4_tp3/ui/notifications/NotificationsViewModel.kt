package com.example.dr4_tp3.ui.notifications

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.dr4_tp3.model.ListaFavorito
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsViewModel : ViewModel() {

    var listaFavoritos: ListaFavorito? = null
    val firebaseStore = FirebaseFirestore.getInstance()


    fun verificarNulo(
        view: View, context: Context
    ): Boolean {
        //Verifico se algum campo está nulo ou vazio
        if (
            view.txtNomeLista.text.isNullOrBlank()
        ) {
            return false
        } else {
            //Caso tudo ocorra ok, começo a alimentar o view model com o resto das informações
            listaFavoritos = ListaFavorito(
                nomeListaFavorito = view.txtNomeLista.text.toString()
            )
            return true
        }

    }

    fun salvarNoFirestore(context: Context){

        var collection = firebaseStore.collection("favoritos")

        var lista: MutableMap<String, Any> = HashMap()
        lista["nomeListaFavorito"] = listaFavoritos!!.nomeListaFavorito

        collection.document(listaFavoritos!!.nomeListaFavorito).set(lista)


    }
    fun asdasds(context: Context, view: View){
        var collection = firebaseStore.collection("usuarios")
        collection.add(ListaFavorito(view.txtNomeLista.text.toString()))
            ?.addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                firebaseStore.collection("usuarios").document(documentReference.id).collection("lista").add(view.txtNomeLista.text.toString())
            }?.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun fireAddStudentToClassroom(view: View) {

        var studentsClassroomRef =
            firebaseStore.collection("usuarios").document("lele@gmail.com")
                .collection("listaFavorito")

        studentsClassroomRef
            .document(view.txtNomeLista.text.toString())
            .set({})

    }

}