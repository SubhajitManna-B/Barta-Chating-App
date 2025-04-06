package com.subhajit.barta.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.subhajit.barta.BartaViewModel
import com.subhajit.barta.CommonProgressBar
import com.subhajit.barta.DestinationScreen
import com.subhajit.barta.R
import com.subhajit.barta.CheckUserSignedIn
import com.subhajit.barta.navigateTo
import com.subhajit.barta.showToast


@Composable
fun SignUpScreen(navController: NavController, vm: BartaViewModel) {

    //Check the user already signed in
    CheckUserSignedIn(navController, vm)

    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
        Column (modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            )
            , horizontalAlignment = Alignment.CenterHorizontally)
        {

            val nameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val numberState = remember {
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }

            Image(painter = painterResource(id = R.drawable.app_icon)
                ,contentDescription = "Barta icon",
                modifier = Modifier.width(200.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
            Text(text = "Sign Up",
                style = TextStyle(color = Color.Black,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(value = nameState.value,
                onValueChange = {nameState.value = it},
                label = { Text("Name")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .focusable(true)
            )
            OutlinedTextField(value = numberState.value,
                onValueChange = {numberState.value = it},
                label = { Text("Phone No.")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            OutlinedTextField(value = emailState.value,
                onValueChange = {emailState.value = it},
                label = { Text("Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth().
                    padding(horizontal = 30.dp)
            )
            OutlinedTextField(value = passwordState.value,
                onValueChange = {passwordState.value = it},
                label = { Text("Password")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
            Button(onClick = {
                //Check email/number and password field are filled or not
                if(nameState.value.text.isEmpty() || emailState.value.text.isEmpty() || numberState.value.text.isEmpty() || passwordState.value.text.isEmpty()){
                    showToast(context, "Please Enter all the Details😑")
                }else{
                    vm.signup(
                        nameState.value.text,
                        numberState.value.text,
                        emailState.value.text,
                        passwordState.value.text
                    )
                }},
                modifier = Modifier.padding(top = 16.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Sign Up",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ))
            }
            Text(text = "Already a user? Login",
                modifier = Modifier.padding(8.dp)
                    .clickable { navigateTo(navController, DestinationScreen.Login.route) },
                style = TextStyle(color = Color.Black,
                    fontSize = 16.sp))
        }
        if (vm.inProgress.value){
            CommonProgressBar()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSignUp(){
    Box(modifier = Modifier.background(Color.White)
        .fillMaxSize()){
        Column {
            SignUpScreen(rememberNavController(), hiltViewModel<BartaViewModel>())
        }
    }
}