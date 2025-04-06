package com.subhajit.barta

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

//Function to navigate screen from one to another screen
fun navigateTo( navController: NavController, route: String ){
    navController.navigate(route){
        popUpTo(route) { inclusive = true }
        launchSingleTop = true
    }
}

//Function to shoe progress bar
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommonProgressBar(){
    Row(modifier = Modifier
        .alpha(.5f)
        .background(Color.LightGray)
        .fillMaxSize()
        .clickable(enabled = false) {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}

//Function to show toast message
fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//Function to check user already sign in or not
@Composable
fun CheckUserSignedIn(navController: NavController, vm: BartaViewModel){
    val alreadySignedIn = remember { mutableStateOf(false) }
    val signIn = vm.signIn.value

    if(signIn && !alreadySignedIn.value){
        alreadySignedIn.value = true
        navController.navigate(DestinationScreen.ChatList.route) {
            popUpTo("chatList") { inclusive = true }
            launchSingleTop = true
        }
    }
}